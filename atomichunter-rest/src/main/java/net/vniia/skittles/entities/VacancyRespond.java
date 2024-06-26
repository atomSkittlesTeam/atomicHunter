package net.vniia.skittles.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.vniia.skittles.dto.VacancyRespondDto;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacancyRespond {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long vacancyId;
    private String coverLetter;

    private String pathToResume;

    private boolean archive;

    private String email;
    //    private String fullName;
    private String lastName;
    private String firstName;
    private long averageScore;

    private long competenceScoreCount;

    public VacancyRespond(VacancyRespondDto vacancyRespondDto) {
        this.update(vacancyRespondDto);
    }

    public void update(VacancyRespondDto vacancyRespondDto) {
        this.vacancyId = vacancyRespondDto.getVacancyId();
        this.coverLetter = vacancyRespondDto.getCoverLetter();
        this.pathToResume = vacancyRespondDto.getPathToResume();
        this.email = vacancyRespondDto.getEmail();
//        this.fullName = vacancyRespondDto.getFullName();
        this.lastName = vacancyRespondDto.getLastName();
        this.firstName = vacancyRespondDto.getFirstName();
        this.averageScore = vacancyRespondDto.getAverageScore();
        this.competenceScoreCount = vacancyRespondDto.getCompetenceScoreCount();
    }

    public void archive() {
        this.archive = true;
    }
}
