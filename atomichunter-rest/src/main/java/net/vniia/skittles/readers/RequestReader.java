package net.vniia.skittles.readers;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.RequestDto;
import net.vniia.skittles.entities.QRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Deprecated
public class RequestReader {

    public static final QRequest request = QRequest.request;

    private final JPAQueryFactory queryFactory;

    private JPAQuery<RequestDto> requestQuery() {
        return queryFactory.from(request)
                .select(Projections.bean(
                        RequestDto.class,
                        request.id,
                        request.requestDate,
                        request.description,
                        request.number,
                        request.priority,
                        request.releaseDate,
                        request.archive

                ));
    }

    public List<RequestDto> getAllRequest(boolean showArchive) {
        return requestQuery()
                .where(showArchive ? null : request.archive.eq(false))
                .fetch();
    }

    public RequestDto getRequestById(Long requestId) {
        return requestQuery()
                .where(request.id.eq(requestId))
                .fetchFirst();
    }
}
