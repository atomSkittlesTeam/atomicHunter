package net.vniia.skittles.readers;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.*;
import net.vniia.skittles.entities.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class VacancyReader {
    private static final QVacancy vacancy = QVacancy.vacancy;
    private static final QPosition position = QPosition.position;

    public static final QVacancyRespond vacancyRespond = QVacancyRespond.vacancyRespond;
    public static final QVacancyCompetenceScore vacancyCompetenceScore = QVacancyCompetenceScore.vacancyCompetenceScore;
    public static final QVacancyCompetence vacancyCompetence = QVacancyCompetence.vacancyCompetence;
    public static final QConfirmationToken confirmationToken = QConfirmationToken.confirmationToken1;

    public static final QInterview interview = QInterview.interview;
    public static final QStaffUnit staffUnit = QStaffUnit.staffUnit;

    public static final QEmployee employee = QEmployee.employee;

    public static QBean<VacancyDto> getMappedSelectForVacancyDto() {
        return Projections.bean(
                VacancyDto.class,
                vacancy.id,
                vacancy.name,
                StaffUnitReader.getMappedSelectForStaffUnitDto().as("staffUnit"),
                PositionReader.getMappedSelectForPositionDto().as("position"),
                vacancy.requirements,
                vacancy.responsibilities,
                vacancy.conditions,
                EmployeeReader.getMappedSelectForEmployeeDto().as("hr"),
                vacancy.archive,
                vacancy.closed,
                vacancy.createInstant,
                vacancy.modifyInstant
        );
    }

    private final JPAQueryFactory queryFactory;

    private final VacancyCompetenceReader vacancyCompetenceReader;

    private final StaffUnitReader staffUnitReader;

    private JPAQuery<VacancyDto> vacancyQuery() {
        return queryFactory.from(vacancy)
                .leftJoin(position).on(position.id.eq(vacancy.positionId))
                .leftJoin(staffUnit).on(staffUnit.id.eq(vacancy.staffUnitId))
                .leftJoin(employee).on(employee.id.eq(vacancy.hrId))
                .select(getMappedSelectForVacancyDto());
    }

    public List<VacancyDto> getAllVacancies(boolean showArchive, boolean showClose) {
        return vacancyQuery()
                .where(showArchive ? null : vacancy.archive.eq(false))
                .where(showClose ? null : vacancy.closed.eq(false))
                .fetch();
    }

    public VacancyDto getVacancyById(Long vacancyId) {
        VacancyDto vacancyDto = vacancyQuery()
                .where(vacancy.id.eq(vacancyId))
                .fetchFirst();
        List<CompetenceWeightDto> competenceWeightDtos = vacancyCompetenceReader.getCompetencesForVacancy(vacancyId);
        vacancyDto.setCompetenceWeight(competenceWeightDtos);
        return vacancyDto;
    }

    public static QBean<VacancyRespondDto> getMappedSelectForVacancyRespondDto() {
        return Projections.bean(
                VacancyRespondDto.class,
                vacancyRespond.id,
                vacancyRespond.vacancyId,
                vacancyRespond.coverLetter,
                vacancyRespond.pathToResume,
                vacancyRespond.email,
//                vacancyRespond.fullName,
                vacancyRespond.lastName,
                vacancyRespond.firstName,
                vacancyRespond.archive,
                vacancyRespond.averageScore,
                vacancyRespond.competenceScoreCount,
                interview.id.as("interviewId")
        );
    }

    private JPAQuery<VacancyRespondDto> vacancyRespondQuery() {
        return queryFactory.from(vacancyRespond)
                .leftJoin(vacancy).on(vacancy.id.eq(vacancyRespond.vacancyId))
//                .leftJoin(confirmationToken).on(confirmationToken.vacancyRespondId.eq(vacancyRespond.id))
                .leftJoin(interview).on(interview.vacancyRespondId.eq(vacancyRespond.id))
                .select(getMappedSelectForVacancyRespondDto());
    }

    public VacancyRespondDto getVacancyRespondById(Long vacancyRespondId) {
        return vacancyRespondQuery()
                .where(vacancyRespond.id.eq(vacancyRespondId))
                .fetchFirst();
    }

    public VacancyRespondDto getVacancyRespondByIdWithInterview(Long vacancyRespondId) {
        return vacancyRespondQuery()
                .where(vacancyRespond.id.eq(vacancyRespondId).and(interview.id.isNotNull()))
                .fetchFirst();
    }

    public List<VacancyRespondDto> getVacancyRespondsByVacancyIds(List<Long> vacancyIds, boolean showArchive) {
        return vacancyRespondQuery()
                .where(vacancyRespond.vacancyId.in(vacancyIds))
                .where(showArchive ? null : vacancyRespond.archive.eq(false))
                .orderBy(vacancyRespond.averageScore.desc())
                .fetch();
    }

    // поиск по vacancyCompetenceScoreId веса
//    public List<VacancyCompetenceScoreDto> getVacancyCompetenceScoreWithWeight(
//            List<VacancyCompetenceScoreDto> list) {
//        return queryFactory.from(vacancyCompetenceScore)
//                .leftJoin(vacancyCompetence).on(vacancyCompetence.id.eq(vacancyCompetenceScore.vacancyCompetenceId))
//                .where(vacancyCompetenceScore.id.in(list.stream().map(VacancyCompetenceScoreDto::getId).toList()))
//                .select(getMappedSelectForCompetenceScoreDto()).fetch();
//    }

    public static QBean<VacancyCompetenceScoreDto> getMappedSelectForCompetenceScoreDto() {
        return Projections.bean(
                VacancyCompetenceScoreDto.class,
                vacancyCompetenceScore.id,
                vacancyCompetenceScore.vacancyCompetenceId,
                vacancyCompetenceScore.employeeId,
                vacancyCompetenceScore.score,
                vacancyCompetenceScore.vacancyRespondId,
                vacancyCompetenceScore.interviewId,
                vacancyCompetence.weight.as("vacancyCompetenceWeight")
        );
    }
}
