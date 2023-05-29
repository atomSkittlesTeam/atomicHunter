package net.vniia.skittles.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacancyDto {
    private Long id;
    private PositionDto position;

    private String salary;

    private String experience;

    private String additional;

    private boolean archive;

    private Instant createInstant;

    private Instant modifyInstant;

    private List<CompetenceWeightDto> competenceWeight;
}
