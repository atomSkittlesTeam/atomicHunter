package net.vniia.skittles.services;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.InterviewDto;
import net.vniia.skittles.dto.UserDto;
import net.vniia.skittles.dto.VacancyWithVacancyRespondDto;
import net.vniia.skittles.entities.Interview;
import net.vniia.skittles.entities.User;
import net.vniia.skittles.entities.VacancyRespond;
import net.vniia.skittles.repositories.InterviewRepository;
import net.vniia.skittles.repositories.VacancyRespondRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class InviteService {

    private final EmailService emailService;

    private final VacancyRespondRepository vacancyRespondRepository;

    private final ReportService reportService;

    private final UserService userService;

    private final InterviewRepository interviewRepository;

    public void inviteToInterview(Long vacancyRespondId, InterviewDto interviewDto) throws Exception {
        VacancyRespond vacancyRespond = vacancyRespondRepository.findById(vacancyRespondId)
                .orElseThrow(() -> new RuntimeException("Отклик на вакансию не найден!"));

        Interview interview = new Interview(vacancyRespondId, interviewDto);
        interviewRepository.save(interview);

        this.emailService.sendInterviewInvite("Приглашение на собеседование!",
                vacancyRespond.getEmail(), vacancyRespond.getId());
    }

    public void sendOffer(VacancyWithVacancyRespondDto vacancyWithVacancyRespondDto) throws MessagingException, IOException {
        VacancyRespond vacancyRespond = vacancyRespondRepository.findById(vacancyWithVacancyRespondDto.getVacancyRespond().getId())
                .orElseThrow(() -> new RuntimeException("Отклик на вакансию не найден!"));
        User currentChief = userService.getCurrentUser();
        UserDto currentHR = new UserDto();
        currentHR.setFullName("(заглушка) Сидоров Артем");
        if (currentChief == null) {
            throw new RuntimeException("Не найден текущий пользователь");
        }

        this.reportService.createPdfAndSendByEmail(vacancyWithVacancyRespondDto, currentChief, currentHR);
    }
}
