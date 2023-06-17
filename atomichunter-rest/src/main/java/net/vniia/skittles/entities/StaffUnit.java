package net.vniia.skittles.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.vniia.skittles.dto.StaffUnitDto;
import net.vniia.skittles.enums.StaffUnitStatus;

import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffUnit {
    @Id
    private UUID id;
    private String positionId;
    private UUID employeeId;
    @Enumerated(EnumType.STRING)
    private StaffUnitStatus status;
    private Instant closeTime;

    public StaffUnit(StaffUnitDto staffUnitDto) {
        this.setId(staffUnitDto.getId());
        this.setPositionId(staffUnitDto.getPositionId());
        this.setEmployeeId(staffUnitDto.getEmployeeId());
        this.setStatus(staffUnitDto.getStatus());
        this.setCloseTime(staffUnitDto.getCloseTime());
    }
}
