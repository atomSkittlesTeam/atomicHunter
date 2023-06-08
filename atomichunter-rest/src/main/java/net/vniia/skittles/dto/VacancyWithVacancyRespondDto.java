package net.vniia.skittles.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacancyWithVacancyRespondDto {
    VacancyDto vacancy;
    VacancyRespondDto vacancyRespond;
}
