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

import java.util.ArrayList;
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
        ArrayList<CompetenceGroup> generatedCompetencesGroup = new ArrayList<>();
        ArrayList<Competence> generatedCompetences = new ArrayList<>();
        ArrayList<Competence> generatedCompetences2 = new ArrayList<>();
        ArrayList<Competence> generatedCompetences3 = new ArrayList<>();
        ArrayList<Competence> generatedCompetences4 = new ArrayList<>();

        long id = 10L;
        long idCom = 10L;
        for (String  cmpName : VacancyTemplate.allCompetencesGroupNames) {
            CompetenceGroup competenceGroup = new CompetenceGroup(id++, cmpName);
            generatedCompetencesGroup.add(competenceGroup);
        }
        competenceGroupRepository.saveAllAndFlush(generatedCompetencesGroup);

        for (String  cmpName : VacancyTemplate.first) {
            Competence competenceGroup = new Competence(idCom++, cmpName, 10L);
            generatedCompetences.add(competenceGroup);
        }
        competenceRepository.saveAllAndFlush(generatedCompetences);

        for (String  cmpName : VacancyTemplate.second) {
            Competence competenceGroup = new Competence(idCom++, cmpName, 11L);
            generatedCompetences2.add(competenceGroup);
        }
        competenceRepository.saveAllAndFlush(generatedCompetences2);

        for (String  cmpName : VacancyTemplate.third) {
            Competence competenceGroup = new Competence(idCom++, cmpName, 12L);
            generatedCompetences3.add(competenceGroup);
        }
        competenceRepository.saveAllAndFlush(generatedCompetences3);

        for (String  cmpName : VacancyTemplate.fourth) {
            Competence competenceGroup = new Competence(idCom++, cmpName, 13L);
            generatedCompetences4.add(competenceGroup);
        }
        competenceRepository.saveAllAndFlush(generatedCompetences4);
    }
}
