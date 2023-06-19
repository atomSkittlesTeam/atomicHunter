package net.vniia.skittles.dto;

import lombok.Data;

@Data
public class InterviewCalendarDto {
    private InterviewDto interview;
    private VacancyDto vacancy;
    private String members;
}
