package net.vniia.skittles.readers;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.PositionDto;
import net.vniia.skittles.entities.QPosition;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PositionReader {

    public static QBean<PositionDto> getMappedSelectForPositionDto() {
        return Projections.bean(PositionDto.class,
                position.id,
                position.name,
                position.description);
    }

    public static final QPosition position = QPosition.position;

    private final JPAQueryFactory queryFactory;

    private JPAQuery<PositionDto> getPositionQuery() {
        return queryFactory.from(position)
                .select(getMappedSelectForPositionDto());
    }

    public List<PositionDto> getAllPositions() {
        return getPositionQuery()
                .fetch();
    }
}
