package net.vniia.skittles.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.vniia.skittles.entities.StaffUnit;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacancyDto {

    private Long id;

    // наименование вакансии
    private String name;

    // штатка
    private StaffUnitDto staffUnit;

    // должность
    private PositionDto position;

    // описание требований к кандидату
    private String requirements;

    // описание обязанностей
    private String responsibilities;

    // предлагаемые условия работы
    private String conditions;

    // сотрудник кадровой службы ответственный за вакансию (join с employId)
    private EmployeeDto hr;

    private boolean archive;

    private boolean closed;

    private Instant createInstant;

    private Instant modifyInstant;

    private List<CompetenceWeightDto> competenceWeight;
}
