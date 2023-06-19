package net.vniia.skittles.services;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.UserDto;
import net.vniia.skittles.dto.VacancyDto;
import net.vniia.skittles.dto.VacancyWithVacancyRespondDto;
import net.vniia.skittles.entities.User;
import net.vniia.skittles.entities.VacancyRespond;
import net.vniia.skittles.readers.VacancyReader;
import net.vniia.skittles.repositories.VacancyRespondRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class OfferService {

    private final ReportService reportService;

    private final UserService userService;

    private final VacancyRespondRepository vacancyRespondRepository;

    private final VacancyReader vacancyReader;

    public void sendOffer(VacancyWithVacancyRespondDto vacancyWithVacancyRespondDto, String mode ) throws MessagingException, IOException {
        VacancyRespond vacancyRespond = vacancyRespondRepository.findById(vacancyWithVacancyRespondDto.getVacancyRespond().getId())
                .orElseThrow(() -> new RuntimeException("Отклик на вакансию не найден!"));

        User currentChief = userService.getCurrentUser();
        UserDto currentHR = new UserDto();
        currentHR.setFullName(vacancyWithVacancyRespondDto.getVacancy().getHr().getFirstName() + " " + vacancyWithVacancyRespondDto.getVacancy().getHr().getLastName());
        if (currentChief == null) {
            throw new RuntimeException("Не найден текущий пользователь");
        }

        this.reportService.createPdfAndSendByEmail(vacancyWithVacancyRespondDto, currentChief, currentHR, mode);
    }
    public void sendOfferDecline(VacancyWithVacancyRespondDto vacancyWithVacancyRespondDto, String mode) throws MessagingException, IOException {
        VacancyRespond vacancyRespond = vacancyRespondRepository.findById(vacancyWithVacancyRespondDto.getVacancyRespond().getId())
                .orElseThrow(() -> new RuntimeException("Отклик на вакансию не найден!"));
        User currentChief = userService.getCurrentUser();
        UserDto currentHR = new UserDto();
        currentHR.setFullName(vacancyWithVacancyRespondDto.getVacancy().getHr().getFirstName() + " " + vacancyWithVacancyRespondDto.getVacancy().getHr().getLastName());
        if (currentChief == null) {
            throw new RuntimeException("Не найден текущий пользователь");
        }

        this.reportService.createPdfAndSendByEmail(vacancyWithVacancyRespondDto, currentChief, currentHR, mode);
    }
}
