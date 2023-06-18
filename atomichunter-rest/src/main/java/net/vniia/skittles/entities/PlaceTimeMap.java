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
public class PlaceTimeMap {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long placeId;
    private Instant dateStart;
    private Instant dateEnd;
    private Long interviewId;

    public PlaceTimeMap(InterviewDto interviewDto, Long interviewId) {
        this.placeId = interviewDto.getPlace().getId();
        this.dateStart = interviewDto.getDateStart();
        this.dateEnd = interviewDto.getDateEnd();
        this.interviewId = interviewId;
    }
}
