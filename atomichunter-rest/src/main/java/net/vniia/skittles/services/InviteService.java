package net.vniia.skittles.services;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.VacancyRespondDto;
import net.vniia.skittles.entities.VacancyRespond;
import net.vniia.skittles.repositories.VacancyRespondRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class InviteService {

    private final EmailService emailService;

    private final VacancyRespondRepository vacancyRespondRepository;

    private final OfferService offerService;

    public void inviteToInterview(VacancyRespondDto vacancyRespondDto) throws Exception {
        VacancyRespond vacancyRespond = vacancyRespondRepository.findById(vacancyRespondDto.getId())
                .orElseThrow(() -> new RuntimeException("Отклик на вакансию не найден!"));

        this.emailService.sendCalendarInvite("Приглашение на собеседование!",
                vacancyRespond.getEmail(), vacancyRespond.getId());
    }

    public void sendOffer(VacancyRespondDto vacancyRespondDto) throws MessagingException, IOException {
        VacancyRespond vacancyRespond = vacancyRespondRepository.findById(vacancyRespondDto.getId())
                .orElseThrow(() -> new RuntimeException("Отклик на вакансию не найден!"));

        this.offerService.createPdfAndSendByEmail(vacancyRespond.getEmail());
    }
}
