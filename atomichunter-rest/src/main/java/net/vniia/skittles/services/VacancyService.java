package net.vniia.skittles.services;

import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.*;
import net.vniia.skittles.entities.Vacancy;
import net.vniia.skittles.entities.VacancyCompetence;
import net.vniia.skittles.entities.VacancyCompetenceScore;
import net.vniia.skittles.entities.VacancyRespond;
import net.vniia.skittles.integration.OrgStructIntegrationService;
import net.vniia.skittles.readers.CompetenceReader;
import net.vniia.skittles.readers.VacancyReader;
import net.vniia.skittles.repositories.VacancyCompetenceRepository;
import net.vniia.skittles.repositories.VacancyCompetenceScoreRepository;
import net.vniia.skittles.repositories.VacancyRepository;
import net.vniia.skittles.repositories.VacancyRespondRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.vniia.skittles.services.ReportService.VACANCY_REPORT_PATH;

@Service
@RequiredArgsConstructor
public class VacancyService {
    private final VacancyReader vacancyReader;

    private final VacancyRepository vacancyRepository;

    private final VacancyRespondRepository vacancyRespondRepository;

    private final MessageService messageService;

    private final ReportService reportService;

    private final VacancyCompetenceRepository vacancyCompetenceRepository;

    private final VacancyCompetenceScoreRepository vacancyCompetenceScoreRepository;

    private final CompetenceReader competenceReader;

    private final OrgStructIntegrationService orgStructIntegrationService;

    @Transactional
    public VacancyDto createVacancy(VacancyDto vacancyDto) {
        this.validateStaffUnit(vacancyDto);
        Vacancy vacancy = new Vacancy(vacancyDto);
        vacancy = this.vacancyRepository.save(vacancy);
        this.messageService.createMessagesForAllUsers(vacancy.getId(),
                "Новая вакансия №" + vacancy.getId());
        this.messageService.createTelegramMessagesForAllUsers("Новая вакансия №" + vacancy.getId());

        List<VacancyCompetence> vacancyCompetences = new ArrayList<>();
        for (CompetenceWeightDto competenceWeightDto : vacancyDto.getCompetenceWeight()) {
            VacancyCompetence vacancyCompetence = new VacancyCompetence(vacancy.getId(), competenceWeightDto);
            vacancyCompetences.add(vacancyCompetence);
        }
        vacancyCompetenceRepository.saveAll(vacancyCompetences);

        return vacancyReader.getVacancyById(vacancy.getId());
    }

    private void validateStaffUnit(VacancyDto vacancyDto) {
        List<Vacancy> vacancies = vacancyRepository.findAllByStaffUnitId(
                vacancyDto.getStaffUnit().getId()
        );
        if (!vacancies.isEmpty()) {
            if (vacancies.stream().anyMatch(e -> !e.isArchive())) {
                throw new RuntimeException("На данную штатную единицу уже есть открытая вакансия! " +
                        "Обработайте или удалите существующую вакансию!");
            }
        }
    }

    @Transactional
    public VacancyDto updateVacancy(Long id, VacancyDto vacancyDto) {
        Vacancy vacancy = this.vacancyRepository.findById(id).orElseThrow(
                () -> {
                    throw new RuntimeException("Вакансия не найдена!");
                }
        );
        vacancy.update(vacancyDto, false);

//        List<VacancyCompetence> vacancyCompetences = vacancyCompetenceRepository.findAllByVacancyId(vacancy.getId());
//        vacancyCompetenceRepository.deleteAll(vacancyCompetences);
//
//        List<VacancyCompetence> cmp = new ArrayList<>();
//        for (CompetenceWeightDto competenceWeightDto : vacancyDto.getCompetenceWeight()) {
//            VacancyCompetence vacancyCompetence = new VacancyCompetence(vacancy.getId(), competenceWeightDto);
//            cmp.add(vacancyCompetence);
//        }
//        vacancyCompetenceRepository.saveAll(cmp);


        List<VacancyCompetence> vacancyCompetences = vacancyCompetenceRepository.findAllByVacancyId(vacancy.getId());

        Map<Long, VacancyCompetence> newMap = vacancyDto.getCompetenceWeight()
                .stream()
                .map(c -> new VacancyCompetence(vacancy.getId(), c))
                .collect(Collectors.toMap(VacancyCompetence::getCompetenceId, Function.identity()));

        List<VacancyCompetence> deletedCompetence = new ArrayList<>();
        for (VacancyCompetence existingVacancyCompetence : vacancyCompetences) {
            if (newMap.containsKey(existingVacancyCompetence.getCompetenceId())) {
                VacancyCompetence competence = newMap.get(existingVacancyCompetence.getCompetenceId());
                existingVacancyCompetence.setWeight(competence.getWeight());
                newMap.remove(existingVacancyCompetence.getCompetenceId());
            } else {
                deletedCompetence.add(existingVacancyCompetence);
            }
        }
        vacancyCompetenceRepository.deleteAll(deletedCompetence);
        vacancyCompetences.removeAll(deletedCompetence);
        vacancyCompetences.addAll(newMap.values());
        vacancyCompetenceRepository.saveAll(vacancyCompetences);

        return vacancyReader.getVacancyById(vacancy.getId());
    }

    @Transactional
    public void archiveVacancy(Long id) {
        Vacancy vacancy = this.vacancyRepository.findById(id).orElseThrow(
                () -> {
                    throw new RuntimeException("Вакансия не найдена!");
                }
        );
        vacancy.archive();
    }

    @Transactional
    public void closeVacancy(Long id, Long vacancyRespondId) {
        Vacancy vacancy = this.vacancyRepository.findById(id).orElseThrow(
                () -> {
                    throw new RuntimeException("Вакансия не найдена!");
                }
        );
        VacancyRespond vacancyRespond = this.vacancyRespondRepository.findById(vacancyRespondId).orElseThrow(
                () -> {
                    throw new RuntimeException("Кандидат на вакансию не найден!");
                }
        );
        vacancy.closed();
        orgStructIntegrationService.registerEmployee(vacancy, vacancyRespond);
    }

    @Transactional
    public VacancyRespondDto createVacancyRespond(VacancyRespondDto vacancyRespondDto) {
        VacancyRespond vacancyRespond = new VacancyRespond(vacancyRespondDto);
        vacancyRespond = this.vacancyRespondRepository.save(vacancyRespond);
        return vacancyReader.getVacancyRespondById(vacancyRespond.getId());
    }

    @Transactional
    public VacancyRespondDto updateVacancyRespond(Long id, VacancyRespondDto vacancyRespondDto) {
        VacancyRespond vacancyRespond = this.vacancyRespondRepository.findById(id).orElseThrow(
                () -> {
                    throw new RuntimeException("Отклик на вакансию не найден!");
                }
        );
        vacancyRespond.update(vacancyRespondDto);
        return vacancyReader.getVacancyRespondById(vacancyRespond.getId());
    }

    @Transactional
    public void updateVacancyRespondAverageScore(Long vacancyRespondId,
                                                 List<VacancyCompetenceScoreDto> vacancyCompetenceScoreDtos) {
        VacancyRespond vacancyRespond = this.vacancyRespondRepository.findById(vacancyRespondId).orElseThrow(
                () -> {
                    throw new RuntimeException("Отклик на вакансию не найден!");
                }
        );
        Long scoresSum = 0L;
        for (VacancyCompetenceScoreDto score : vacancyCompetenceScoreDtos) {
            scoresSum += score.getScore() * score.getWeight();
        }
        vacancyRespond.setAverageScore((scoresSum + vacancyRespond.getAverageScore() * vacancyRespond.getCompetenceScoreCount()) / (vacancyRespond.getCompetenceScoreCount() + 1));
        vacancyRespond.setCompetenceScoreCount(vacancyRespond.getCompetenceScoreCount() + 1);
        vacancyRespondRepository.saveAndFlush(vacancyRespond);
    }

    @Transactional
    public void archiveVacancyRespond(Long vacancyRespondId) {
        VacancyRespond vacancyRespond = this.vacancyRespondRepository.findById(vacancyRespondId).orElseThrow(
                () -> {
                    throw new RuntimeException("Вакансия не найдена!");
                }
        );
        vacancyRespond.archive();
    }

    @Transactional
    public List<String> createVacancyReportAndReturnPath(
            Long vacancyId,String additionalInformation) throws IOException {
        VacancyDto vacancyDto = vacancyReader.getVacancyById(vacancyId);
        String pathToPdf = reportService.createVacancyReport(vacancyDto,
                additionalInformation);
        return Collections.singletonList(pathToPdf);
    }

    public HttpEntity<byte[]> getVacancyReportFileByPath(@RequestBody String path) throws IOException {
        path = VACANCY_REPORT_PATH + path;
        byte[] model = org.apache.commons.io.FileUtils.readFileToByteArray(new File(path));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentLength(model.length);
        return new HttpEntity<byte[]>(model, headers);
    }

    public Boolean validateVacancyCompetenceScore(UUID employeeId, Long vacancyRespondId) {
        List<VacancyCompetenceScore> vacancyCompetenceScores = vacancyCompetenceScoreRepository
                .findAllByEmployeeIdAndVacancyRespondId(employeeId, vacancyRespondId);
        return (vacancyCompetenceScores.size() == 0);

    }

    public List<VacancyCompetenceScoreDto> createVacancyCompetenceScore(VacancyCompetenceScoreRequestDto requestDto) {
        List<VacancyCompetenceScore> vacancyCompetenceScores = new ArrayList<>();
        requestDto.getCompetenceWeightScoreList().forEach(e -> vacancyCompetenceScores.add(
                new VacancyCompetenceScore(requestDto, e)));
        List<VacancyCompetenceScore> savedScores = vacancyCompetenceScoreRepository.saveAllAndFlush(vacancyCompetenceScores);
        List<VacancyCompetenceScoreDto> result = new ArrayList<>();
        savedScores.forEach(e -> result.add(new VacancyCompetenceScoreDto(e)));
        return result;
    }

    public List<CompetenceWeightScoreFullDto> getVacancyRespondAnalysis(Long vacancyId, List<Long> checkedIds) {
        List<CompetenceWeightScoreFullDto> listWithEmployees =
                competenceReader.getVacancyCompetenceScoreForVacancy(vacancyId, checkedIds);

        Function<CompetenceWeightScoreFullDto, String> key = e ->
                e.getVacancyRespond().getId() + "r"
                + e.getCompetence().getId() + "c"
                + e.getWeight() + "w";

        Map<String, List<CompetenceWeightScoreFullDto>> map =
                listWithEmployees.stream().collect(Collectors.groupingBy(key));

        List<CompetenceWeightScoreFullDto> finalArray = new ArrayList<>();
        for (Map.Entry<String, List<CompetenceWeightScoreFullDto>> stringListEntry : map.entrySet()) {
            List<Long> scores = stringListEntry
                    .getValue()
                    .stream()
                    .map(CompetenceWeightScoreFullDto::getScore)
                    .toList();
            Long averageSum = scores.stream()
                    .reduce(0L, Long::sum) / scores.size();

            CompetenceWeightScoreFullDto comp = stringListEntry.getValue().get(0);
            comp.setScore(averageSum);
            comp.getVacancyRespond().setAverageScore(averageSum*comp.getWeight());
            finalArray.add(comp);
        }

        return finalArray;
    }

    public List<CompetenceWeightScoreFullDto> getVacancyRespondAnalysisForVacancyRespondId(Long vacancyId,
                                                                                           Long vacancyRespondId) {
        List<CompetenceWeightScoreFullDto> scores = this.getVacancyRespondAnalysis(vacancyId,
                Collections.singletonList(vacancyRespondId));
        return scores.stream().filter(e -> e.getScore() < 4L).toList();
    }

    public String getGoodVacancyForRespondId(Long vacancyId,
                                             Long vacancyRespondId) {
        // get good scores from
        List<CompetenceWeightScoreFullDto> scores = this.getVacancyRespondAnalysis(vacancyId,
                Collections.singletonList(vacancyRespondId));
        List<Long> goodCompetenceIds = scores.stream()
                .filter(e -> e.getScore() > 4L)
                .map(c -> c.getCompetence().getId())
                .toList();

        List<Vacancy> vacancies = competenceReader.findVacanciesByCompetences(goodCompetenceIds, vacancyId);


        return (vacancies != null && !vacancies.isEmpty()) ?
                vacancies.stream().map(Vacancy::getName).collect(Collectors.joining(", "))
                : null;
    }
}
