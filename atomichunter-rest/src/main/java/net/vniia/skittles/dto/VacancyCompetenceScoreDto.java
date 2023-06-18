package net.vniia.skittles.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.vniia.skittles.entities.VacancyCompetenceScore;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacancyCompetenceScoreDto {
    private Long id;
    private Long vacancyCompetenceId;
    private Long vacancyRespondId;
    private Long interviewId;
    private UUID employeeId;
    private Long score;
    private Long weight;
    private String comment;

    public VacancyCompetenceScoreDto(VacancyCompetenceScore vacancyCompetenceScore) {
        this.id = vacancyCompetenceScore.getId();
        this.vacancyCompetenceId = vacancyCompetenceScore.getVacancyCompetenceId();
        this.employeeId = vacancyCompetenceScore.getEmployeeId();
        this.vacancyRespondId = vacancyCompetenceScore.getVacancyRespondId();
        this.interviewId = vacancyCompetenceScore.getInterviewId();
        this.score = vacancyCompetenceScore.getScore();
        this.weight = vacancyCompetenceScore.getWeight();
        this.comment = vacancyCompetenceScore.getComment();
    }
}
