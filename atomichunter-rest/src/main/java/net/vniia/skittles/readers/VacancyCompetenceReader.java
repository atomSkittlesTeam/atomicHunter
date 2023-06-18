package net.vniia.skittles.readers;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.CompetenceWeightDto;
import net.vniia.skittles.entities.QCompetence;
import net.vniia.skittles.entities.QVacancyCompetence;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class VacancyCompetenceReader {

    private static final QVacancyCompetence vacancyCompetence = QVacancyCompetence.vacancyCompetence;

    private static final QCompetence competence = QCompetence.competence;

    private final JPAQueryFactory queryFactory;


    public static QBean<CompetenceWeightDto> getMappedSelectForCompetenceWeightDto() {
        return Projections.bean(
                CompetenceWeightDto.class,
                CompetenceReader.getMappedSelectForCompetenceDto().as("competence"),
                vacancyCompetence.weight
        );
    }

    private JPAQuery<CompetenceWeightDto> competenceWeightDtoJPAQuery(Long vacancyId) {
        return queryFactory.from(vacancyCompetence)
                .leftJoin(competence).on(competence.id.eq(vacancyCompetence.competenceId))
                .select(getMappedSelectForCompetenceWeightDto())
                .where(vacancyCompetence.vacancyId.eq(vacancyId));
    }


    public List<CompetenceWeightDto> getCompetencesForVacancy(Long vacancyId) {
        return this.competenceWeightDtoJPAQuery(vacancyId).fetch();
    }
}
