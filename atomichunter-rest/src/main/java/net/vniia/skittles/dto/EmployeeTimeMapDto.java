package net.vniia.skittles.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
public class EmployeeTimeMapDto {
    private Long id;
    private UUID employeeId;
    private Instant dateStart;
    private Instant dateEnd;
    private Long interviewId;
}
