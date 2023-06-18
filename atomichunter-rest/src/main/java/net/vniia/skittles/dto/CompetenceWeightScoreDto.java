package net.vniia.skittles.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompetenceWeightScoreDto {
    private CompetenceDto competence;
    private Long vacancyCompetenceId;
    private long weight;
    private long score;
    private String comment;
}
