package net.vniia.skittles.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.vniia.skittles.dto.VacancyCompetenceScoreDto;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacancyCompetenceScore {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long vacancyCompetenceId;

    private Long vacancyRespondId;

    private Long interviewId;

    private Long maintainerId;

    private Integer score;

    public VacancyCompetenceScore(VacancyCompetenceScoreDto vacancyCompetenceScoreDto) {
        this.vacancyCompetenceId = vacancyCompetenceScoreDto.getVacancyCompetenceId();
        this.maintainerId = vacancyCompetenceScoreDto.getMaintainerId();
        this.vacancyRespondId = vacancyCompetenceScoreDto.getVacancyRespondId();
        this.interviewId = vacancyCompetenceScoreDto.getInterviewId();
        this.score = vacancyCompetenceScoreDto.getScore();
    }
}
