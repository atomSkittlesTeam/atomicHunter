package net.vniia.skittles.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class PlaceTimeMapDto {
    private Long id;
    private Long placeId;
    private Instant dateStart;
    private Instant dateEnd;
    private Long interviewId;
}
