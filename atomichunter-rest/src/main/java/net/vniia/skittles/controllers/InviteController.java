package net.vniia.skittles.controllers;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.VacancyDto;
import net.vniia.skittles.dto.VacancyRespondDto;
import net.vniia.skittles.services.InviteService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("invite")
@RequiredArgsConstructor
public class InviteController {

    private final InviteService inviteService;

    @PostMapping("interview")
    @Transactional
    public void inviteToInterview(@RequestBody VacancyRespondDto vacancyRespondDto) throws Exception {
        // @TODO отправлять дату начала собеса
        this.inviteService.inviteToInterview(vacancyRespondDto);
    }
}
