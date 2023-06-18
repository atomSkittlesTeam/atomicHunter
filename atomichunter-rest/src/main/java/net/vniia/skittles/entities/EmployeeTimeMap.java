package net.vniia.skittles.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.vniia.skittles.dto.EmployeeDto;
import net.vniia.skittles.dto.InterviewDto;
import net.vniia.skittles.dto.PositionDto;

import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeTimeMap {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private UUID employeeId;
    private Instant dateStart;
    private Instant dateEnd;

    public EmployeeTimeMap (EmployeeDto employeeDto, InterviewDto interviewDto) {
        this.employeeId = employeeDto.getId();
        this.dateStart = interviewDto.getDateStart();
        this.dateEnd = interviewDto.getDateEnd();
    }
}
