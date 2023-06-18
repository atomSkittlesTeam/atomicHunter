package net.vniia.skittles.services;

import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.InterviewDto;
import net.vniia.skittles.entities.Interview;
import net.vniia.skittles.entities.VacancyRespond;
import net.vniia.skittles.repositories.InterviewRepository;
import net.vniia.skittles.repositories.VacancyRespondRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InterviewService {

    private final EmailService emailService;

    private final VacancyRespondRepository vacancyRespondRepository;

    private final InterviewRepository interviewRepository;

    public void createInterview(Long vacancyRespondId, InterviewDto interviewDto) throws Exception {
        VacancyRespond vacancyRespond = vacancyRespondRepository.findById(vacancyRespondId)
                .orElseThrow(() -> new RuntimeException("Отклик на вакансию не найден!"));

        Interview interview = new Interview(vacancyRespondId, interviewDto);
        interviewRepository.save(interview);

        // send to candidate
        this.emailService.sendInterviewInvite("Приглашение на собеседование!",
                vacancyRespond.getEmail(), vacancyRespond.getId());

        // send to employees
    }
}
