package net.vniia.skittles.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.vniia.skittles.dto.InterviewDto;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Interview {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long vacancyRespondId;
    private Long placeId;
    private Instant dateStart;
    private Instant dateEnd;
    public Interview(Long vacancyRespondId, InterviewDto interview) {
        this.vacancyRespondId = vacancyRespondId;
        this.update(interview);
    }

    public void update(InterviewDto interview) {
        this.placeId = interview.getPlace().getId();
        this.dateStart = interview.getDateStart();
        this.dateEnd = interview.getDateEnd();
    }
}
