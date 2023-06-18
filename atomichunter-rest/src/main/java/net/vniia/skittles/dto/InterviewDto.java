package net.vniia.skittles.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
public class InterviewDto {
    private Long id;
    private VacancyRespondDto vacancyRespond;
    private String meeting;
    private Instant dateStart;
    private Instant dateEnd;
    private List<EmployeeDto> employees;
}
