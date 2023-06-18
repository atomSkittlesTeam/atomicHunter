package net.vniia.skittles.generators;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.entities.Competence;
import net.vniia.skittles.entities.CompetenceGroup;
import net.vniia.skittles.repositories.CompetenceGroupRepository;
import net.vniia.skittles.repositories.CompetenceRepository;
import net.vniia.skittles.templates.VacancyTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CompetenceGenerator {

    private final CompetenceRepository competenceRepository;
    private final CompetenceGroupRepository competenceGroupRepository;

    @Value("${generators.competence}")
    private Boolean generatorCompetenceEnable;

    @PostConstruct
    public void runGenerator() {
        if (this.generatorCompetenceEnable) {
            List<Competence> competences = competenceRepository.findAll();
            if (competences.isEmpty()) {
                this.generateCompetencesTree();
            }
        }
    }

    private void generateCompetencesTree() {
        long step = 1;
        for (String cmpName : VacancyTemplate.allCompetencesGroupNames) {
            // step does not saved as id!!!
            CompetenceGroup competenceGroup = new CompetenceGroup(step, cmpName);
            competenceGroup = competenceGroupRepository.save(competenceGroup);
            for (String c : getCompetencesForGroupNumber(step)) {
                Competence cmp = new Competence(step, c, false, competenceGroup.getId());
                competenceRepository.save(cmp);
            }
            step++;
        }
    }

    private List<String> getCompetencesForGroupNumber(long stepNumber) {
        if (stepNumber == 1) {
            return VacancyTemplate.first;
        } else if (stepNumber == 2) {
            return VacancyTemplate.second;
        } else if (stepNumber == 3) {
            return VacancyTemplate.third;
        } else if (stepNumber == 4) {
            return VacancyTemplate.fourth;
        }
        return null;
    }
}