package net.vniia.skittles.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacancyAnalysisDto {
    private VacancyRespondDto vacancyRespond;
    private CompetenceDto competence;
}
