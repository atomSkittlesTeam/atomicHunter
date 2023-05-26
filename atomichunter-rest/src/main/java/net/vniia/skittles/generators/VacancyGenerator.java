package net.vniia.skittles.generators;

import jakarta.annotation.PostConstruct;
import net.vniia.skittles.entities.*;
import net.vniia.skittles.repositories.*;
import net.vniia.skittles.templates.VacancyTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class VacancyGenerator {

    @Value("${generators.vacancy}")
    private Boolean generatorVacancyEnable;

    @Value("${generators.vacancy-positions}")
    private Boolean generatorVacancyPositionsEnable;

    @Value("${generators.vacancy-positions-online}")
    private Boolean generatorVacancyPositionsOnlineEnable;

    @Autowired
    private VacancyRepository vacancyRepository;

    @Autowired
    private VacancyPositionRepository vacancyPositionRepository;

    private static int vacancyCount = 15;

    @PostConstruct
    public void generateData() {
        this.generateVacancy();
        this.generateVacancyPositions();
    }

    private void generateVacancy() {
        if (generatorVacancyEnable) {
            List<Vacancy> requestList = vacancyListGenerate();
            vacancyRepository.saveAllAndFlush(requestList);
        }
    }

    private void generateVacancyPositions() {
        if (generatorVacancyPositionsEnable) {
            List<VacancyRespond> vacancyPositionList = vacancyPositionListGenerate();
            vacancyPositionRepository.saveAll(vacancyPositionList);
        }
    }

    private List<Vacancy> vacancyListGenerate() {
        Random random = new Random();
        List<Vacancy> vacancyList = new ArrayList<>();
        for (int i = 0; i < vacancyCount; i++) {
            Vacancy vacancyOne = new Vacancy(
                    (long) i, //id - автогенерируется, здесь как заглушка для allArgs
                    String.valueOf(VacancyTemplate.allPositionNames.get(random.nextInt(0, VacancyTemplate.allPositionNames.size()))), //name
                    String.valueOf(random.nextInt(0, 10000)) + " рублей", //salary
                    String.valueOf(random.nextInt(20, 30)) + " лет", //experience
                    String.valueOf(VacancyTemplate.allVacancyAdditional.get(random.nextInt(0, VacancyTemplate.allVacancyAdditional.size()))), //additional
                    false
            );
            vacancyList.add(vacancyOne);
        }
        return vacancyList;
    }

    private List<VacancyRespond> vacancyPositionListGenerate() {
        Random random = new Random();
        List<VacancyRespond> vacancyPositionList = new ArrayList<>();
        int index = 0;
        List<Long> vacancyIds = vacancyRepository.findAll().stream().map(Vacancy::getId).toList();
        for (int i = 0; i < vacancyIds.size(); i++) {
            for (int j = 0; j < random.nextInt(4,6); j++) {

                VacancyRespond vacancyRespond = new VacancyRespond(
                        (long) index, //id - автогенерируется, здесь как заглушка для allArgs
                        (long) vacancyIds.get(i), //vacancyId
                        String.valueOf(VacancyTemplate.allVacancyPositionCoverLetter
                                .get(random.nextInt(0, VacancyTemplate.allVacancyPositionCoverLetter.size()))), //cover letter,
                        "//",
                        false); //note
                vacancyPositionList.add(vacancyRespond);
                index++;
            }
        }
        return vacancyPositionList;
    }

    @Scheduled(fixedDelay = 5000)
    private void vacancyPositionGeneratorByScheduler() {
        if (generatorVacancyPositionsOnlineEnable) {
            Random random = new Random();
            List<Long> vacancyIds = vacancyRepository.findAll().stream().map(Vacancy::getId).toList();
            int vacancyIndex = random.nextInt(0, vacancyIds.size());
            VacancyRespond vacancyRespond = new VacancyRespond(
                    (long) 0, //id - автогенерируется, здесь как заглушка для allArgs
                    (long) vacancyIds.get(vacancyIndex), //vacancyId
                    String.valueOf(VacancyTemplate.allVacancyPositionCoverLetter
                            .get(random.nextInt(0, VacancyTemplate.allVacancyPositionCoverLetter.size()))), //cover letter,
                    "//",
                    false);
            vacancyPositionRepository.saveAndFlush(vacancyRespond);
        }
    }
}
