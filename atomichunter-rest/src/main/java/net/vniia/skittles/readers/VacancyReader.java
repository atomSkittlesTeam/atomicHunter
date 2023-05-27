package net.vniia.skittles.readers;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.VacancyDto;
import net.vniia.skittles.entities.QPosition;
import net.vniia.skittles.entities.QVacancy;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class VacancyReader {
    private static final QVacancy vacancy = QVacancy.vacancy;
    private static final QPosition position = QPosition.position;

    public static QBean<VacancyDto> getMappedSelectForVacancyDto() {
        return Projections.bean(
                VacancyDto.class,
                vacancy.id,
                PositionReader.getMappedSelectForPositionDto().as("position"),
                vacancy.salary,
                vacancy.experience,
                vacancy.additional,
                vacancy.archive
        );
    }

    private final JPAQueryFactory queryFactory;

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
        return vacancyQuery()
                .where(vacancy.id.eq(vacancyId))
                .fetchFirst();
    }
}
