package net.vniia.skittles.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CompetenceWeightDto {
    private CompetenceDto competence;
    private long weight;
}
