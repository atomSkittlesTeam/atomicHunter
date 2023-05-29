package net.vniia.skittles.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.vniia.skittles.dto.CompetenceWeightDto;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacancyCompetence {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long vacancyId;

    private Long competenceId;

    private Long weight;

    public VacancyCompetence(Long vacancyId, CompetenceWeightDto competenceWeightDto) {
        this.vacancyId = vacancyId;
        this.competenceId = competenceWeightDto.getCompetence().getId();
        this.weight = competenceWeightDto.getWeight();
    }
}
