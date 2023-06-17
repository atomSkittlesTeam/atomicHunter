package net.vniia.skittles.services;

import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.CompetenceWeightDto;
import net.vniia.skittles.dto.VacancyDto;
import net.vniia.skittles.dto.VacancyRespondDto;
import net.vniia.skittles.entities.Vacancy;
import net.vniia.skittles.entities.VacancyCompetence;
import net.vniia.skittles.entities.VacancyRespond;
import net.vniia.skittles.readers.VacancyReader;
import net.vniia.skittles.repositories.VacancyCompetenceRepository;
import net.vniia.skittles.repositories.VacancyRepository;
import net.vniia.skittles.repositories.VacancyRespondRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VacancyService {
    private final VacancyReader vacancyReader;

    private final VacancyRepository vacancyRepository;

    private final VacancyRespondRepository vacancyRespondRepository;

    private final MessageService messageService;

    private final VacancyCompetenceRepository vacancyCompetenceRepository;

    private void sortList(List<Vacancy> list) {
        // just a sort algo
//        Comparator<Vacancy> c = Comparator.comparing(Request::getRequestDate)
//                .thenComparing(Request::getReleaseDate);
//        list.sort(c);
    }

    @Transactional
    public VacancyDto createVacancy(VacancyDto vacancyDto) {
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

    @Transactional
    public VacancyDto updateVacancy(Long id, VacancyDto vacancyDto) {
        Vacancy vacancy = this.vacancyRepository.findById(id).orElseThrow(
                () -> {
                    throw new RuntimeException("Вакансия не найдена!");
                }
        );
        vacancy.update(vacancyDto);

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
    public void archiveVacancyRespond(Long vacancyRespondId) {
        VacancyRespond vacancyRespond = this.vacancyRespondRepository.findById(vacancyRespondId).orElseThrow(
                () -> {
                    throw new RuntimeException("Вакансия не найдена!");
                }
        );
        vacancyRespond.archive();
    }
}
