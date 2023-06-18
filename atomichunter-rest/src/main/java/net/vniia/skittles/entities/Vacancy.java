package net.vniia.skittles.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.vniia.skittles.dto.EmployeeDto;
import net.vniia.skittles.dto.PositionDto;
import net.vniia.skittles.dto.StaffUnitDto;
import net.vniia.skittles.dto.VacancyDto;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vacancy {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // наименование вакансии
    private String name;

    // id штатки, доставать должность через неё
    private UUID staffUnitId;

    // id должности
    private String positionId;

    // описание требований к кандидату
    private String requirements;

    // описание обязанностей
    private String responsibilities;

    // предлагаемые условия работы
    private String conditions;

    // сотрудник кадровой службы ответственный за вакансию (join с employId)
    private UUID hrId;

    private boolean archive;


    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Instant createInstant;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Instant modifyInstant;

    public Vacancy(VacancyDto vacancyDto) {
        this.update(vacancyDto, true);
    }

    public void update(VacancyDto vacancyDto, Boolean updatePositionAndStaffUnit) {
        this.name = vacancyDto.getName();
        if (updatePositionAndStaffUnit) {
            this.staffUnitId = Optional.ofNullable(vacancyDto.getStaffUnit())
                    .map(StaffUnitDto::getId)
                    .orElse(null);
            this.positionId = Optional.ofNullable(vacancyDto.getPosition())
                    .map(PositionDto::getId)
                    .orElse(null);
        }
        this.requirements = vacancyDto.getRequirements();
        this.responsibilities = vacancyDto.getResponsibilities();
        this.conditions = vacancyDto.getConditions();
        this.hrId = Optional.ofNullable(vacancyDto.getHr())
                .map(EmployeeDto::getId)
                .orElse(null);;
    }

    public void archive() {
        this.archive = true;
    }
}
