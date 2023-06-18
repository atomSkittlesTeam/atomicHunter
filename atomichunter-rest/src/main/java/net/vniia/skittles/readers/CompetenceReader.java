package net.vniia.skittles.readers;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.*;
import net.vniia.skittles.entities.CompetenceGroup;
import net.vniia.skittles.entities.QCompetence;
import net.vniia.skittles.entities.QVacancyCompetence;
import net.vniia.skittles.entities.VacancyRespond;
import net.vniia.skittles.repositories.CompetenceGroupRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CompetenceReader {

    private static final QCompetence competence = QCompetence.competence;

    private static final QVacancyCompetence vacancyCompetence = QVacancyCompetence.vacancyCompetence;

    public static QBean<CompetenceDto> getMappedSelectForCompetenceDto() {
        return Projections.bean(
                CompetenceDto.class,
                competence.id,
                competence.name,
                competence.groupId
        );
    }

    private final JPAQueryFactory queryFactory;

    private final CompetenceGroupRepository competenceGroupRepository;

    private final CompetenceGroupReader competenceGroupReader;

    private final VacancyReader vacancyReader;


    public List<CompetenceDto> getAllCompetences() {
        return queryFactory.from(competence)
                .select(getMappedSelectForCompetenceDto())
                .fetch();
    }

    public List<CompetenceDto> getAllCompetencesByGroupId(Long groupId) {
        CompetenceGroup competenceGroup = this.competenceGroupRepository.findById(groupId).orElseThrow(
                () -> new RuntimeException("Группа навыков не найдена!"));
        return queryFactory.from(competence)
                .select(getMappedSelectForCompetenceDto())
                .where(competence.groupId.eq(groupId))
                .fetch();
    }

    public List<CompetenceGroupsWithCompetencesDto> getAllCompetenceTree() {
        List<CompetenceDto> allCompetences = this.getAllCompetences();
        List<CompetenceGroupDto> allCompetenceGroup = this.competenceGroupReader.getAllGroupCompetences();
        Map<Long, List<CompetenceDto>> competencesMap = allCompetences.stream()
                .collect(Collectors.groupingBy(CompetenceDto::getGroupId));
        List<CompetenceGroupsWithCompetencesDto> competenceGroupsWithCompetencesDtos = new ArrayList<>();
        for (CompetenceGroupDto competenceGroupDto : allCompetenceGroup) {
            List<CompetenceDto> competenceDtos = competencesMap.get(competenceGroupDto.getId());
            if (!competenceDtos.isEmpty()) {
                CompetenceGroupsWithCompetencesDto competenceGroupsWithCompetencesDto =
                        new CompetenceGroupsWithCompetencesDto();
                competenceGroupsWithCompetencesDto.setCompetenceGroup(competenceGroupDto);
                competenceGroupsWithCompetencesDto.setCompetences(competenceDtos);
                competenceGroupsWithCompetencesDtos.add(competenceGroupsWithCompetencesDto);
            }
        }
        return competenceGroupsWithCompetencesDtos;
    }

    public List<CompetenceWeightScoreDto> getVacancyCompetenceScoreCard(Long vacancyRespondId) {
        VacancyRespondDto vacancyRespondDto = vacancyReader.getVacancyRespondById(vacancyRespondId);
        Long vacancyId = vacancyRespondDto.getVacancyId();

        List<CompetenceWeightScoreDto> weightScoreDtos =
                queryFactory.from(vacancyCompetence)
                .innerJoin(competence).on(vacancyCompetence.competenceId.eq(competence.id))
                .where(vacancyCompetence.vacancyId.eq(vacancyId))
            .select(Projections.bean(
                    CompetenceWeightScoreDto.class,
                    CompetenceReader.getMappedSelectForCompetenceDto().as("competence"),
                    vacancyCompetence.weight.as("weight")
            ))
            .fetch();

        return weightScoreDtos;
    }
}
