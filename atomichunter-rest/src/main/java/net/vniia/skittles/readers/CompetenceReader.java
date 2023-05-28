package net.vniia.skittles.readers;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.CompetenceDto;
import net.vniia.skittles.entities.QCompetence;
import net.vniia.skittles.entities.QMatrixCompetence;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CompetenceReader {

    private static final QCompetence competence = QCompetence.competence;

    private static final QMatrixCompetence matrixCompetence = QMatrixCompetence.matrixCompetence;

    public static QBean<CompetenceDto> getMappedSelectForCompetenceDto() {
        return Projections.bean(
                CompetenceDto.class,
                competence.id,
                competence.name
        );
    }

    private final JPAQueryFactory queryFactory;

    public List<CompetenceDto> getCompetencesForPosition(Long positionId) {
        return queryFactory.from(competence)
                .innerJoin(matrixCompetence)
                    .on(matrixCompetence.competenceId.eq(competence.id)
                            .and(matrixCompetence.positionId.eq(positionId)))
                .select(getMappedSelectForCompetenceDto())
                .fetch();
    }

    public List<CompetenceDto> getAllCompetences() {
        return queryFactory.from(competence)
                .select(getMappedSelectForCompetenceDto())
                .fetch();
    }
}