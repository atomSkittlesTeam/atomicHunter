package net.vniia.skittles.generators;

import jakarta.annotation.PostConstruct;
import net.vniia.skittles.entities.*;
import net.vniia.skittles.repositories.*;
import net.vniia.skittles.templates.VacancyTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.Instant;
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
            List<VacancyPosition> vacancyPositionList = vacancyPositionListGenerate();
            vacancyPositionRepository.saveAll(vacancyPositionList);
        }
    }

    private List<Vacancy> vacancyListGenerate() {
        Random random = new Random();
        List<Vacancy> vacancyList = new ArrayList<>();
        for (int i = 0; i < vacancyCount; i++) {
            Vacancy vacancyOne = new Vacancy(
                    (long) i, //id - автогенерируется, здесь как заглушка для allArgs
                    String.valueOf(VacancyTemplate.allVacancyNames.get(random.nextInt(0, VacancyTemplate.allVacancyNames.size()))), //name
                    String.valueOf(random.nextInt(0, 10000)) + " рублей", //salary
                    String.valueOf(random.nextInt(20, 30)) + " лет", //experience
                    String.valueOf(VacancyTemplate.allVacancyAdditional.get(random.nextInt(0, VacancyTemplate.allVacancyAdditional.size()))), //additional
                    false
            );
            vacancyList.add(vacancyOne);
        }
        return vacancyList;
    }

    private List<VacancyPosition> vacancyPositionListGenerate() {
        Random random = new Random();
        List<VacancyPosition> vacancyPositionList = new ArrayList<>();
        int index = 0;
        List<Long> vacancyIds = vacancyRepository.findAll().stream().map(Vacancy::getId).toList();
        for (int i = 0; i < vacancyIds.size(); i++) {
            for (int j = 0; j < random.nextInt(4,6); j++) {

                VacancyPosition vacancyPosition = new VacancyPosition(
                        (long) index, //id - автогенерируется, здесь как заглушка для allArgs
                        (long) vacancyIds.get(i), //vacancyId
                        String.valueOf(VacancyTemplate.allVacancyPositionCoverLetter
                                .get(random.nextInt(0, VacancyTemplate.allVacancyPositionCoverLetter.size()))), //cover letter,
                        "//",
                        false); //note
                vacancyPositionList.add(vacancyPosition);
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
            VacancyPosition vacancyPosition = new VacancyPosition(
                    (long) 0, //id - автогенерируется, здесь как заглушка для allArgs
                    (long) vacancyIds.get(vacancyIndex), //vacancyId
                    String.valueOf(VacancyTemplate.allVacancyPositionCoverLetter
                            .get(random.nextInt(0, VacancyTemplate.allVacancyPositionCoverLetter.size()))), //cover letter,
                    "//",
                    false);
            vacancyPositionRepository.saveAndFlush(vacancyPosition);
        }
    }
}
