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
public class PlaceReader {
    private static final QPlace place = QPlace.place;
//    private static final QPlaceTimeMap placeTimeMap = QPlaceTimeMap.placeTimeMap;

    private final JPAQueryFactory queryFactory;

    public static QBean<PlaceDto> getMappedSelectForPlaceDto() {
        return Projections.bean(
                PlaceDto.class,
                place.id,
                place.name,
                place.archive
//                PlaceTimeMapReader.getMappedSelectForPlaceTimeMapDto()
        );
    }

    private JPAQuery<PlaceDto> placeQuery() {
        return queryFactory.from(place)
//                .leftJoin(placeTimeMap).on(placeTimeMap.placeId.eq(place.id))
                .select(getMappedSelectForPlaceDto());
    }

    public List<PlaceDto> getAllPlaces(boolean showArchive) {
        return placeQuery()
                .where(showArchive ? null : place.archive.eq(false))
                .fetch();
    }

    public PlaceDto getPlaceById(Long placeId) {
        PlaceDto placeDto = placeQuery()
                .where(place.id.eq(placeId))
                .fetchFirst();
        return placeDto;
    }
}
