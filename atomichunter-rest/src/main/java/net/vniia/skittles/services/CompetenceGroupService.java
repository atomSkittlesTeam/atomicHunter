package net.vniia.skittles.services;

import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.CompetenceDto;
import net.vniia.skittles.dto.CompetenceGroupDto;
import net.vniia.skittles.entities.Competence;
import net.vniia.skittles.entities.CompetenceGroup;
import net.vniia.skittles.readers.CompetenceGroupReader;
import net.vniia.skittles.repositories.CompetenceGroupRepository;
import net.vniia.skittles.repositories.CompetenceRepository;
import org.jvnet.hk2.annotations.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class CompetenceGroupService {

    private final CompetenceGroupRepository competenceGroupRepository;

    private final CompetenceRepository competenceRepository;

    private final CompetenceGroupReader competenceGroupReader;

    @Transactional
    public CompetenceGroupDto createCompetenceGroup(CompetenceGroupDto competenceGroupDto) {
        CompetenceGroup competenceGroup = new CompetenceGroup(competenceGroupDto);
        competenceGroup = competenceGroupRepository.save(competenceGroup);
        return competenceGroupReader.getCompetenceGroupById(competenceGroup.getId());
    }

    @Transactional
    public void createCompetence(Long groupId, CompetenceDto competenceDto) {
        CompetenceGroup competenceGroup = competenceGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Группа навыков не найдена!"));
        Competence competence = new Competence(groupId, competenceDto);
        competence = competenceRepository.save(competence);
    }

    @Transactional
    public void updateCompetence(Long competenceId, CompetenceDto competenceDto) {
        Competence competence = competenceRepository.findById(competenceId)
                .orElseThrow(() -> new RuntimeException("Навык не найден!"));
        competence.update(competenceDto);
    }
}
