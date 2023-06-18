package net.vniia.skittles.controllers;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.InterviewCalendarDto;
import net.vniia.skittles.dto.InterviewDto;
import net.vniia.skittles.dto.VacancyWithVacancyRespondDto;
import net.vniia.skittles.readers.InterviewReader;
import net.vniia.skittles.services.InterviewService;
import net.vniia.skittles.services.OfferService;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("interview")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

    private final InterviewReader interviewReader;

    private final OfferService offerService;

    @PostMapping("vacancy-respond/{vacancyRespondId}")
    @Transactional
    public void createInterview(@PathVariable Long vacancyRespondId,
                                @RequestBody InterviewDto interviewDto) throws Exception {
        this.interviewService.createInterview(vacancyRespondId, interviewDto);
    }

    @GetMapping("{interviewId}")
    public InterviewDto getInterviewById(@PathVariable Long interviewId) {
        return interviewReader.getInterviewById(interviewId);
    }

    @PutMapping("{interviewId}")
    @Transactional
    public InterviewDto updateInterviewById(@PathVariable Long interviewId, @RequestBody InterviewDto interviewDto) {
        return this.interviewService.updateInterviewById(interviewId, interviewDto);
    }

    @DeleteMapping("delete/{interviewId}")
    @Transactional
    public void deleteInterviewById(@PathVariable Long interviewId) {
        this.interviewService.deleteInterviewById(interviewId);
    }

    @PostMapping("validate")
    @Transactional
    public List<String> validateInterview(@RequestBody InterviewDto interviewDto) {
        if (interviewDto.getDateStart() != null && interviewDto.getDateEnd() != null
                && interviewDto.getDateStart().isAfter(interviewDto.getDateEnd())) {
            return Collections.singletonList("Даты указаны неверно: дата начала собеседования позже, чем дата конца");
        }
        return this.interviewService.validateInterview(interviewDto);
    }

    @PostMapping("offer")
    @Transactional
    public void sendOffer(@RequestBody VacancyWithVacancyRespondDto vacancyWithVacancyRespondDto) throws Exception {
        this.offerService.sendOffer(vacancyWithVacancyRespondDto);
    }

    @GetMapping("calendar")
    public List<InterviewCalendarDto> getCalendar() {
        return interviewReader.getAllInterviewCalendar();
    }
}
