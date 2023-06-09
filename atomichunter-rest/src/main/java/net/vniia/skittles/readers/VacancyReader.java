package net.vniia.skittles.readers;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.CompetenceWeightDto;
import net.vniia.skittles.dto.VacancyDto;
import net.vniia.skittles.dto.VacancyRespondDto;
import net.vniia.skittles.entities.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class VacancyReader {
    private static final QVacancy vacancy = QVacancy.vacancy;
    private static final QPosition position = QPosition.position;
    public static final QVacancyRespond vacancyRespond = QVacancyRespond.vacancyRespond;
    public static final QConfirmationToken confirmationToken = QConfirmationToken.confirmationToken1;
    public static final QInterview interview = QInterview.interview;

    public static QBean<VacancyDto> getMappedSelectForVacancyDto() {
        return Projections.bean(
                VacancyDto.class,
                vacancy.id,
                PositionReader.getMappedSelectForPositionDto().as("position"),
                vacancy.salary,
                vacancy.experience,
                vacancy.additional,
                vacancy.archive,
                vacancy.createInstant,
                vacancy.modifyInstant
        );
    }

    private final JPAQueryFactory queryFactory;

    private final VacancyCompetenceReader vacancyCompetenceReader;

    private JPAQuery<VacancyDto> vacancyQuery() {
        return queryFactory.from(vacancy)
                .leftJoin(position).on(position.id.eq(vacancy.positionId))
                .select(getMappedSelectForVacancyDto());
    }

    public List<VacancyDto> getAllVacancies(boolean showArchive) {
        return vacancyQuery()
                .where(showArchive ? null : vacancy.archive.eq(false))
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
                vacancyRespond.fullName,
                vacancyRespond.archive,
                interview.id.as("interviewId"),
                confirmationToken.accepted.as("interviewInviteAccepted")
        );
    }

    private JPAQuery<VacancyRespondDto> vacancyRespondQuery() {
        return queryFactory.from(vacancyRespond)
                .leftJoin(vacancy).on(vacancy.id.eq(vacancyRespond.vacancyId))
                .leftJoin(confirmationToken).on(confirmationToken.vacancyRespondId.eq(vacancyRespond.id))
                .leftJoin(interview).on(interview.vacancyRespondId.eq(vacancyRespond.id))
                .select(getMappedSelectForVacancyRespondDto());
    }

    public List<VacancyRespondDto> getVacancyRespondsByIds(List<Long> vacancyIds, boolean showArchive) {
        return vacancyRespondQuery()
                .where(vacancyRespond.vacancyId.in(vacancyIds))
                .where(showArchive ? null : vacancyRespond.archive.eq(false))
                .fetch();
    }
}
