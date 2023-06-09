package net.vniia.skittles.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacancyRespondDto {
    private Long id;
    private Long vacancyId;
    private String coverLetter;
    private String pathToResume;
    private boolean archive;
    private String email;
    private String fullName;
    private boolean interviewInviteAccepted;
    private Long interviewId;
}
