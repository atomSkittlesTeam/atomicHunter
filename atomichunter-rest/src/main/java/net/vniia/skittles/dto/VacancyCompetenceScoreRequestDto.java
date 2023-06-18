package net.vniia.skittles.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.vniia.skittles.entities.VacancyCompetenceScore;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacancyCompetenceScoreRequestDto {
    private Long vacancyRespondId;
    private Long interviewId;
    private EmployeeDto employee;
    private List<CompetenceWeightScoreDto> competenceWeightScoreList;
    private String comment;
}
