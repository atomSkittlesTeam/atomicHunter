package net.vniia.skittles.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.vniia.skittles.dto.VacancyDto;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vacancy {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String salary;

    private String experience;

    private String additional;

    private boolean archive;

    public Vacancy(VacancyDto vacancyDto) {
        this.update(vacancyDto);
    }

    public void update(VacancyDto vacancyDto) {
        this.salary = vacancyDto.getSalary();
        this.experience = vacancyDto.getExperience();
        this.additional = vacancyDto.getAdditional();
    }

    public void archive() {
        this.archive = true;
    }
}
