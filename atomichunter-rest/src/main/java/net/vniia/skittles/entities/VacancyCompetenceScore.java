package net.vniia.skittles.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.vniia.skittles.dto.CompetenceWeightScoreDto;
import net.vniia.skittles.dto.VacancyCompetenceScoreDto;
import net.vniia.skittles.dto.VacancyCompetenceScoreRequestDto;

import java.util.UUID;

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
    private UUID employeeId;
    private Long score;
    private Long weight;
    private String comment;

    public VacancyCompetenceScore(VacancyCompetenceScoreDto vacancyCompetenceScoreDto) {
        this.vacancyCompetenceId = vacancyCompetenceScoreDto.getVacancyCompetenceId();
        this.employeeId = vacancyCompetenceScoreDto.getEmployeeId();
        this.vacancyRespondId = vacancyCompetenceScoreDto.getVacancyRespondId();
        this.interviewId = vacancyCompetenceScoreDto.getInterviewId();
        this.score = vacancyCompetenceScoreDto.getScore();
        this.weight = vacancyCompetenceScoreDto.getWeight();
        this.comment = vacancyCompetenceScoreDto.getComment();
    }

    public VacancyCompetenceScore(VacancyCompetenceScoreRequestDto requestDto, CompetenceWeightScoreDto competenceWeightScoreDto) {
        this.vacancyCompetenceId = competenceWeightScoreDto.getVacancyCompetenceId();
        this.score = competenceWeightScoreDto.getScore();
        this.weight = competenceWeightScoreDto.getWeight();
        this.vacancyRespondId = requestDto.getVacancyRespondId();
        this.interviewId = requestDto.getInterviewId();
        this.employeeId = requestDto.getEmployee().getId();
        this.comment = requestDto.getComment();
    }
}
