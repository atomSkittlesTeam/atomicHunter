package net.vniia.skittles.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacancyDto {
    private Long id;

    private String salary;

    private String experience;

    private String additional;

    private boolean archive;
}
