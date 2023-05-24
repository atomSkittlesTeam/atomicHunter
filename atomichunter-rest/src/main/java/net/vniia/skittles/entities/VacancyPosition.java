package net.vniia.skittles.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.vniia.skittles.dto.VacancyDto;
import net.vniia.skittles.dto.VacancyPositionDto;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacancyPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long vacancyId;
    private String coverLetter;

    private String pathToResume;

    private boolean archive;

    public VacancyPosition(VacancyPositionDto vacancyPositionDto) {
        this.update(vacancyPositionDto);
    }

    public void update(VacancyPositionDto vacancyPositionDto) {
        this.vacancyId = vacancyPositionDto.getVacancyId();
        this.coverLetter = vacancyPositionDto.getCoverLetter();
        this.pathToResume = vacancyPositionDto.getPathToResume();
    }

    public void archive() {
        this.archive = true;
    }
}
