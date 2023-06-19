package net.vniia.skittles.readers;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.*;
import net.vniia.skittles.entities.*;
import net.vniia.skittles.repositories.CompetenceGroupRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CompetenceReader {

    private static final QCompetence competence = QCompetence.competence;

    private static final QVacancyCompetence vacancyCompetence = QVacancyCompetence.vacancyCompetence;

    private static final QEmployee employee = QEmployee.employee;
    private static final QInterview interview = QInterview.interview;

    private static final QVacancyCompetenceScore vacancyCompetenceScore = QVacancyCompetenceScore.vacancyCompetenceScore;

    private static final QVacancyRespond vacancyRespond = QVacancyRespond.vacancyRespond;

    public static QBean<CompetenceDto> getMappedSelectForCompetenceDto() {
        return Projections.bean(
                CompetenceDto.class,
                competence.id,
                competence.name,
                competence.groupId,
                competence.binaryLogic
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
            if (competenceDtos != null && !competenceDtos.isEmpty()) {
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
                    vacancyCompetence.weight.as("weight"),
                    vacancyCompetence.id.as("vacancyCompetenceId")
            ))
            .fetch();

        return weightScoreDtos;
    }

    public List<EmployeeDto> getEmployeesWithScoreForRespond(Long vacancyRespondId) {
        List<EmployeeDto> employeeDtos = queryFactory.from(employee)
                .innerJoin(vacancyCompetenceScore).on(vacancyCompetenceScore.employeeId.eq(employee.id)
                        .and(vacancyCompetenceScore.vacancyRespondId.eq(vacancyRespondId)))
                .select(EmployeeReader.getMappedSelectForEmployeeDto())
                .distinct()
                .fetch();
        employeeDtos.forEach(e -> {
            e.setEmployeeFullName(e.getLastName() + " " + e.getFirstName());
        });
        return employeeDtos;
    }

    public List<CompetenceWeightScoreDto> getVacancyCompetenceScoreForEmployee(Long vacancyRespondId, UUID employeeId) {
        List<CompetenceWeightScoreDto> weightScoreDtos =
                queryFactory.from(vacancyCompetenceScore)
                        .innerJoin(vacancyCompetence).on(vacancyCompetence.id.eq(vacancyCompetenceScore.vacancyCompetenceId))
                        .innerJoin(competence).on(competence.id.eq(vacancyCompetence.competenceId))
                        .where(vacancyCompetenceScore.vacancyRespondId.eq(vacancyRespondId)
                                .and(vacancyCompetenceScore.employeeId.eq(employeeId)))
                        .select(Projections.bean(
                                CompetenceWeightScoreDto.class,
                                CompetenceReader.getMappedSelectForCompetenceDto().as("competence"),
                                vacancyCompetenceScore.weight.as("weight"),
                                vacancyCompetenceScore.score.as("score"),
                                vacancyCompetenceScore.vacancyCompetenceId,
                                vacancyCompetenceScore.comment
                        ))
                        .fetch();

        return weightScoreDtos;
    }

    public List<CompetenceWeightScoreFullDto> getVacancyCompetenceScoreForVacancy(Long vacancyId, List<Long> checkedIds) {
        JPAQuery<CompetenceWeightScoreFullDto> weightScoreDtos =
            queryFactory.from(vacancyCompetenceScore)
                    .innerJoin(vacancyCompetence).on(vacancyCompetence.id.eq(vacancyCompetenceScore.vacancyCompetenceId))
                    .innerJoin(competence).on(competence.id.eq(vacancyCompetence.competenceId))
                    .innerJoin(vacancyRespond).on(vacancyRespond.id.eq(vacancyCompetenceScore.vacancyRespondId))
                    .leftJoin(interview).on(interview.vacancyRespondId.eq(vacancyRespond.id))
                    .where(vacancyCompetence.vacancyId.eq(vacancyId))
                    .select(Projections.bean(
                            CompetenceWeightScoreFullDto.class,
                            CompetenceReader.getMappedSelectForCompetenceDto().as("competence"),
                            VacancyReader.getMappedSelectForVacancyRespondDto().as("vacancyRespond"),
                            vacancyCompetenceScore.weight.as("weight"),
                            vacancyCompetenceScore.score.as("score"),
                            vacancyCompetenceScore.employeeId.as("employeeId")
                    ));
        if (!checkedIds.isEmpty()) {
            weightScoreDtos.where(vacancyRespond.id.in(checkedIds));
        }
        return weightScoreDtos.fetch();
    }
}
