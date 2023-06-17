package net.vniia.skittles.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.vniia.skittles.entities.VacancyCompetenceScore;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacancyCompetenceScoreDto {
    private Long id;
    private Long vacancyCompetenceId;
    private Long vacancyRespondId;
    private Long interviewId;
    private Long maintainerId;
    private Integer score;
    private Integer vacancyCompetenceWeight; //дозаполняется

    public VacancyCompetenceScoreDto(VacancyCompetenceScore vacancyCompetenceScore) {
        this.id = vacancyCompetenceScore.getId();
        this.vacancyCompetenceId = vacancyCompetenceScore.getVacancyCompetenceId();
        this.maintainerId = vacancyCompetenceScore.getMaintainerId();
        this.vacancyRespondId = vacancyCompetenceScore.getVacancyRespondId();
        this.interviewId = vacancyCompetenceScore.getInterviewId();
        this.score = vacancyCompetenceScore.getScore();
    }
}
