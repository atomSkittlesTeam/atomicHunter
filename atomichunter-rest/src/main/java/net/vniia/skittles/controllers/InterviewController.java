package net.vniia.skittles.controllers;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.InterviewDto;
import net.vniia.skittles.dto.VacancyWithVacancyRespondDto;
import net.vniia.skittles.readers.InterviewReader;
import net.vniia.skittles.services.InviteService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("interview")
@RequiredArgsConstructor
public class InterviewController {

    private final InviteService inviteService;

    private final InterviewReader interviewReader;

    @PostMapping("vacancy-respond/{vacancyRespondId}")
    @Transactional
    public void inviteToInterview(@PathVariable Long vacancyRespondId,
                                  @RequestBody InterviewDto interviewDto) throws Exception {
        this.inviteService.inviteToInterview(vacancyRespondId, interviewDto);
    }

    @GetMapping("{interviewId}")
    @Transactional
    public InterviewDto getInterviewById(@PathVariable Long interviewId) {
        return interviewReader.getInterviewById(interviewId);
    }

    @PostMapping("offer")
    @Transactional
    public void sendOffer(@RequestBody VacancyWithVacancyRespondDto vacancyWithVacancyRespondDto) throws Exception {
        this.inviteService.sendOffer(vacancyWithVacancyRespondDto);
    }
}
