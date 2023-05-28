package net.vniia.skittles.dto;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacancyDto {
    private Long id;
    private PositionDto position;

    private String salary;

    private String experience;

    private String additional;

    private boolean archive;

    private Instant createInstant;

    private Instant modifyInstant;
}
