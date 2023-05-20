package net.vniia.skittles.readers;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.ProductDto;
import net.vniia.skittles.dto.RequestPositionDto;
import net.vniia.skittles.entities.QProduct;
import net.vniia.skittles.entities.QRequestPosition;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RequestPositionReader {

    private final JPAQueryFactory queryFactory;

    private static final QProduct product = QProduct.product;
    private static final QRequestPosition requestPosition = QRequestPosition.requestPosition;

    private JPAQuery<RequestPositionDto> getRequestPositionQuery() {
        return queryFactory.from(requestPosition)
                .select(getMappedSelectForRequestPositionDto())
                .leftJoin(product).on(product.id.eq(requestPosition.productId));
    }

    private QBean<RequestPositionDto> getMappedSelectForRequestPositionDto() {
        return Projections.bean(
                RequestPositionDto.class,
                requestPosition.id,
                requestPosition.requestId,
                requestPosition.note,
                Projections.bean(ProductDto.class,
                        product.id,
                        product.designation,
                        product.name).as("product"), // обязательно писать alias, иначе будет ошибка
                requestPosition.archive);
    }

    public List<RequestPositionDto> getRequestPositionsByRequestId(Long requestId, boolean showArchive) {
        return getRequestPositionQuery()
                .where(requestPosition.requestId.eq(requestId)
                        .and(showArchive ? null : requestPosition.archive.eq(false))
                )
                .fetch();
    }

    public RequestPositionDto getRequestPositionById(Long requestPositionId) {
        return getRequestPositionQuery()
                .where(requestPosition.id.eq(requestPositionId))
                .fetchFirst();
    }
}
