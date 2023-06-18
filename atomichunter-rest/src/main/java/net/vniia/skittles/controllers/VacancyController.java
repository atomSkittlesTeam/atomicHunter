package net.vniia.skittles.controllers;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.*;
import net.vniia.skittles.readers.VacancyReader;
import net.vniia.skittles.services.VacancyService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("vacancy")
@RequiredArgsConstructor
public class VacancyController {
    private final VacancyService vacancyService;

    private final VacancyReader vacancyReader;

    @GetMapping("all")
    public List<VacancyDto> getAllVacancies(@RequestParam boolean showArchive) {
        return vacancyReader.getAllVacancies(showArchive);
    }

    @GetMapping("{vacancyId}")
    @Transactional
    public VacancyDto getVacancyById(@PathVariable Long vacancyId) {
        return vacancyReader.getVacancyById(vacancyId);
    }

    @PostMapping("create")
    @Transactional
    public VacancyDto createVacancy(@RequestBody VacancyDto vacancyDto) {
        return this.vacancyService.createVacancy(vacancyDto);
    }

    @PutMapping("{id}/update")
    @Transactional
    public VacancyDto updateVacancy(@PathVariable Long id, @RequestBody VacancyDto vacancyDto) {
        return this.vacancyService.updateVacancy(id, vacancyDto);
    }

    @DeleteMapping("{id}/archive")
    @Transactional
    public void archiveVacancy(@PathVariable Long id) {
        this.vacancyService.archiveVacancy(id);
    }

    @GetMapping("respond/{vacancyRespondId}")
    @Transactional
    public VacancyRespondDto getVacancyRespondById(@PathVariable Long vacancyRespondId) {
        return vacancyReader.getVacancyRespondById(vacancyRespondId);
    }

    @PostMapping("respond/get-all-by-ids")
    public List<VacancyRespondDto> getVacancyRespondsByIds(@RequestBody List<Long> vacancyIds,
                                                         @RequestParam boolean showArchive) {
        return vacancyReader.getVacancyRespondsByVacancyIds(vacancyIds, showArchive);
    }

    @PostMapping("respond/create")
    @Transactional
    public VacancyRespondDto createVacancy(@RequestBody VacancyRespondDto vacancyRespondDto) {
        return this.vacancyService.createVacancyRespond(vacancyRespondDto);
    }

    @PutMapping("respond/{vacancyRespondId}/update")
    @Transactional
    public VacancyRespondDto updateVacancy(@PathVariable Long vacancyRespondId, @RequestBody VacancyRespondDto vacancyRespondDto) {
        return this.vacancyService.updateVacancyRespond(vacancyRespondId, vacancyRespondDto);
    }

    @DeleteMapping("respond/{vacancyRespondId}/archive")
    @Transactional
    public void archiveVacancyRespond(@PathVariable Long vacancyRespondId) {
        this.vacancyService.archiveVacancyRespond(vacancyRespondId);
    }

    @PostMapping("{vacancyId}/report")
    @Transactional
    public List<String> createVacancyReportAndReturnPath(@PathVariable Long vacancyId,
                                                         @RequestBody(required = false) String additionalInformation)
            throws IOException {
        return this.vacancyService.createVacancyReportAndReturnPath(vacancyId, additionalInformation);
    }

    @GetMapping("competence-score/{employeeId}/{vacancyRespondId}/validation")
    @Transactional
    public Boolean validateVacancyCompetenceScore(@PathVariable UUID employeeId, @PathVariable Long vacancyRespondId) {
        return this.vacancyService.validateVacancyCompetenceScore(employeeId, vacancyRespondId);
    }

    @PostMapping("competence-score/add")
    @Transactional
    public void validateVacancyCompetenceScore(@RequestBody VacancyCompetenceScoreRequestDto requestDto) {
        List<VacancyCompetenceScoreDto> scoreDtosWithIds = this.vacancyService.createVacancyCompetenceScore(requestDto);
        this.vacancyService.updateVacancyRespondAverageScore(requestDto.getVacancyRespondId(), scoreDtosWithIds);
    }

    @GetMapping("vacancy/{vacancyId}/analysis")
    public List<VacancyAnalysisDto> getVacancyRespondAnalysis(@PathVariable Long vacancyId) {
        return vacancyService.getVacancyRespondAnalysis(vacancyId);
    }
}
