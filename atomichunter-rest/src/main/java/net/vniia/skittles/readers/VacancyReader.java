package net.vniia.skittles.readers;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.VacancyDto;
import net.vniia.skittles.entities.QVacancy;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class VacancyReader {
    public static final QVacancy vacancy = QVacancy.vacancy;

    private final JPAQueryFactory queryFactory;

    private JPAQuery<VacancyDto> vacancyQuery() {
        return queryFactory.from(vacancy)
                .select(Projections.bean(
                        VacancyDto.class,
                        vacancy.id,
                        vacancy.name,
                        vacancy.salary,
                        vacancy.experience,
                        vacancy.additional,
                        vacancy.archive
                ));
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
