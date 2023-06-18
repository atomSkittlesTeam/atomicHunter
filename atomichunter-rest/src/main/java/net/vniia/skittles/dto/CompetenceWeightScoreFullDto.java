package net.vniia.skittles.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompetenceWeightScoreFullDto {
    private CompetenceDto competence;

    private VacancyRespondDto vacancyRespond;
    private Long vacancyCompetenceId;
    private long weight;
    private long score;
    private String comment;
    private UUID employeeId;
}
