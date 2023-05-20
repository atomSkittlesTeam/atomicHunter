package net.vniia.skittles.controllers;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.RequestDto;
import net.vniia.skittles.dto.VacancyDto;
import net.vniia.skittles.readers.RequestReader;
import net.vniia.skittles.readers.VacancyReader;
import net.vniia.skittles.services.RequestService;
import net.vniia.skittles.services.VacancyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
