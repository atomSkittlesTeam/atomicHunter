package net.vniia.skittles.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.vniia.skittles.entities.StaffUnit;
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
    private EmployeeDto employee;
    private StaffUnitStatus status;
    private Instant closeTime;
    private Long vacancyId;

    public StaffUnitDto(StaffUnit staffUnit) {
        this.id = staffUnit.getId();
        this.positionId = staffUnit.getPositionId();
        this.employeeId = staffUnit.getEmployeeId();
        this.status = staffUnit.getStatus();
        this.closeTime = staffUnit.getCloseTime();
    }
}
