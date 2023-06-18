package net.vniia.skittles.controllers;

import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.*;
import net.vniia.skittles.readers.CompetenceGroupReader;
import net.vniia.skittles.readers.CompetenceReader;
import net.vniia.skittles.services.CompetenceGroupService;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("competence")
@RequiredArgsConstructor
public class CompetenceController {

    private final CompetenceReader competenceReader;

    private final CompetenceGroupReader competenceGroupReader;

    private final CompetenceGroupService competenceGroupService;

//    @GetMapping("position/{positionId}")
//    public List<CompetenceDto> getCompetencesForPosition(@PathVariable Long positionId) {
//        return competenceReader.getCompetencesForPosition(positionId);
//    }

    // competences
    @GetMapping("all")
    public List<CompetenceDto> getAllCompetences() {
        return competenceReader.getAllCompetences();
    }

    @PostMapping("group/{groupId}")
    @Transactional
    public void createCompetence(@RequestBody CompetenceDto competenceDto, @PathVariable Long groupId) {
        this.competenceGroupService.createCompetence(groupId, competenceDto);
    }

    @PutMapping("{competenceId}")
    @Transactional
    public void updateCompetenceById(@PathVariable Long competenceId, @RequestBody CompetenceDto competenceDto) {
        this.competenceGroupService.updateCompetence(competenceId, competenceDto);
    }
    @PutMapping("group/{competenceGroupId}/update")
    @Transactional
    public void updateCompetenceById(@PathVariable Long competenceGroupId, @RequestBody CompetenceGroupDto competenceGroupDto) {
        this.competenceGroupService.updateCompetenceGroup(competenceGroupId, competenceGroupDto);
    }

    // groups

    @GetMapping("group/all")
    public List<CompetenceGroupDto> getAllGroupCompetences() {
        return competenceGroupReader.getAllGroupCompetences();
    }

    @PostMapping("group")
    @Transactional
    public CompetenceGroupDto createCompetenceGroup(@RequestBody CompetenceGroupDto competenceGroupDto) {
        return competenceGroupService.createCompetenceGroup(competenceGroupDto);
    }

    @GetMapping("group/{groupId}")
    public List<CompetenceDto> getAllCompetencesByGroupId(@PathVariable Long groupId) {
        return competenceReader.getAllCompetencesByGroupId(groupId);
    }

    @GetMapping("allTree")
    public List<CompetenceGroupsWithCompetencesDto> getAllCompetencesTree() {
        return competenceReader.getAllCompetenceTree();
    }

    @GetMapping("{vacancyRespondId}")
    public List<CompetenceWeightScoreDto> getCompetenceWeight(@PathVariable Long vacancyRespondId) {
        return competenceReader.getVacancyCompetenceScoreCard(vacancyRespondId);
    }
}
