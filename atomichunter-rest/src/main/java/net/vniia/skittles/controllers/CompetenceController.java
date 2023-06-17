package net.vniia.skittles.controllers;

import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.CompetenceDto;
import net.vniia.skittles.dto.CompetenceGroupDto;
import net.vniia.skittles.readers.CompetenceGroupReader;
import net.vniia.skittles.readers.CompetenceReader;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("competence")
@RequiredArgsConstructor
public class CompetenceController {

    private final CompetenceReader competenceReader;

    private final CompetenceGroupReader competenceGroupReader;

    @GetMapping("position/{positionId}")
    public List<CompetenceDto> getCompetencesForPosition(@PathVariable Long positionId) {
        return competenceReader.getCompetencesForPosition(positionId);
    }

    @GetMapping("all")
    public List<CompetenceDto> getAllCompetences() {
        return competenceReader.getAllCompetences();
    }

    @GetMapping("group/all")
    public List<CompetenceGroupDto> getAllGroupCompetences() {
        return competenceGroupReader.getAllGroupCompetences();
    }

    @GetMapping("group/{id}")
    public List<CompetenceDto> getAllCompetencesByGroupId(@PathVariable Long groupId) {
        return competenceReader.getAllCompetencesByGroupId(groupId);
    }
}
