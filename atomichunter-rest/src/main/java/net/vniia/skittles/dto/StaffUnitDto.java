package net.vniia.skittles.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.vniia.skittles.enums.StaffUnitStatus;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffUnitDto {
    private UUID id;
    private String positionId;
    private PositionDto position;
    private UUID employeeId;
    private StaffUnitStatus status;
    private Instant closeTime;
}
