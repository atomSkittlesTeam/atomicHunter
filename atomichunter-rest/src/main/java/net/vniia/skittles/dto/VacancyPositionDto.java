package net.vniia.skittles.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacancyPositionDto {
    private Long id;
    private Long vacancyId;
    private String coverLetter;
    private String pathToResume;
    private boolean archive;
}
