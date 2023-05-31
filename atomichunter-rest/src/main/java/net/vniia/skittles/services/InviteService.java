package net.vniia.skittles.services;

import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.VacancyRespondDto;
import net.vniia.skittles.entities.VacancyRespond;
import net.vniia.skittles.repositories.VacancyRespondRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InviteService {

    private final EmailService emailService;

    private final VacancyRespondRepository vacancyRespondRepository;

    public void inviteToInterview(VacancyRespondDto vacancyRespondDto) throws Exception {
        VacancyRespond vacancyRespond = vacancyRespondRepository.findById(vacancyRespondDto.getId())
                .orElseThrow(() -> new RuntimeException("Отклик на вакансию не найден!"));

        this.emailService.sendCalendarInvite("Приглашение на собеседование!",
                vacancyRespond.getEmail(), vacancyRespond.getId());
    }
}
