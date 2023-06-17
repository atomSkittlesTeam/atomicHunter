package net.vniia.skittles.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CompetenceWeightScoreDto {
    private CompetenceDto competence;
    private Integer score;
}
