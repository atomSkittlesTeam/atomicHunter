package net.vniia.skittles.controllers;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.VacancyWithVacancyRespondDto;
import net.vniia.skittles.services.OfferService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("offer")
@RequiredArgsConstructor
public class OfferController {

    private final OfferService offerService;

    @PostMapping("approve")
    @Transactional
    public void sendOffer(@RequestBody VacancyWithVacancyRespondDto vacancyWithVacancyRespondDto) throws Exception {
        this.offerService.sendOffer(vacancyWithVacancyRespondDto, "approve");
    }

    @PostMapping("decline")
    @Transactional
    public void sendDeclineOffer(@RequestBody VacancyWithVacancyRespondDto vacancyWithVacancyRespondDto) throws Exception {
        this.offerService.sendOffer(vacancyWithVacancyRespondDto, "decline");
    }

    @PostMapping("alternative")
    @Transactional
    public void sendAlternativeOffer(@RequestBody VacancyWithVacancyRespondDto vacancyWithVacancyRespondDto) throws Exception {
        this.offerService.sendOffer(vacancyWithVacancyRespondDto, "alternative");
    }
}
