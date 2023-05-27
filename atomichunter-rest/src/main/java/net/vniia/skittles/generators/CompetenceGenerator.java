package net.vniia.skittles.generators;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.entities.Competence;
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

    @Value("${generators.competence}")
    private Boolean generatorCompetenceEnable;

    @PostConstruct
    public void runGenerator() {
        if (this.generatorCompetenceEnable) {
            List<Competence> competences = competenceRepository.findAll();
            if (competences.isEmpty()) {
                List<Competence> generatedCompetences = this.generateCompetences();
            }
        }
    }

    private List<Competence> generateCompetences() {
        ArrayList<Competence> generatedCompetences = new ArrayList<>();
        long id = 1L;
        for (String  cmpName : VacancyTemplate.allCompetencesNames) {
            Competence competence = new Competence(id++, cmpName);
            generatedCompetences.add(competence);
        }
        competenceRepository.saveAllAndFlush(generatedCompetences);
        return generatedCompetences;
    }
}
