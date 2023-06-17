package net.vniia.skittles.readers;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.CompetenceDto;
import net.vniia.skittles.dto.CompetenceGroupDto;
import net.vniia.skittles.entities.QCompetenceGroup;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CompetenceGroupReader {

    private static final QCompetenceGroup competenceGroup = QCompetenceGroup.competenceGroup;

    private final JPAQueryFactory queryFactory;

    public static QBean<CompetenceGroupDto> getMappedSelectForCompetenceGroupDto() {
        return Projections.bean(
                CompetenceGroupDto.class,
                competenceGroup.id,
                competenceGroup.name
        );
    }

    public List<CompetenceGroupDto> getAllGroupCompetences() {
        return queryFactory.from(competenceGroup)
                .select(getMappedSelectForCompetenceGroupDto())
                .fetch();
    }


}
