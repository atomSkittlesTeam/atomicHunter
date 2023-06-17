package net.vniia.skittles.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.vniia.skittles.dto.PositionDto;
import net.vniia.skittles.dto.VacancyDto;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Optional;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vacancy {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long positionId;

    private String salary;

    private String experience;

    private String additional;

    private boolean archive;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Instant createInstant;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Instant modifyInstant;

    public Vacancy(VacancyDto vacancyDto) {
        this.update(vacancyDto);
    }

    public void update(VacancyDto vacancyDto) {
        this.positionId = Optional.ofNullable(vacancyDto.getPosition())
                .map(e -> Long.parseLong(e.getId()))
                .orElse(null);
        this.salary = vacancyDto.getSalary();
        this.experience = vacancyDto.getExperience();
        this.additional = vacancyDto.getAdditional();
    }

    public void archive() {
        this.archive = true;
    }
}
