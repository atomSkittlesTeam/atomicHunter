package net.vniia.skittles.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompetenceWeightScoreDto {
    private CompetenceDto competence;
    private long weight;
    private long score;
}
