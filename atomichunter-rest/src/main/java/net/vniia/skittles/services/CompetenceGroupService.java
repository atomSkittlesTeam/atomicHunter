package net.vniia.skittles.services;

import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.CompetenceGroupDto;
import net.vniia.skittles.entities.CompetenceGroup;
import net.vniia.skittles.readers.CompetenceGroupReader;
import net.vniia.skittles.repositories.CompetenceGroupRepository;
import org.jvnet.hk2.annotations.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class CompetenceGroupService {

    private final CompetenceGroupRepository competenceGroupRepository;

    private final CompetenceGroupReader competenceGroupReader;

    @Transactional
    public CompetenceGroupDto createCompetenceGroup(CompetenceGroupDto competenceGroupDto) {
        CompetenceGroup competenceGroup = new CompetenceGroup(competenceGroupDto);
        competenceGroup = competenceGroupRepository.save(competenceGroup);
        return competenceGroupReader.getCompetenceGroupById(competenceGroup.getId());
    }
}
