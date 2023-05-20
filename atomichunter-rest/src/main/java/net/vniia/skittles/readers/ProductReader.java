package net.vniia.skittles.readers;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.ProductDto;
import net.vniia.skittles.entities.QProduct;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductReader {
    private final JPAQueryFactory queryFactory;

    private static final QProduct product = QProduct.product;

    private JPAQuery<ProductDto> getProductQuery() {
        return queryFactory.from(product)
                .select(getMappedSelectForProductDto());
    }

    private QBean<ProductDto> getMappedSelectForProductDto() {
        return Projections.bean(ProductDto.class,
                        product.id,
                        product.designation,
                        product.name);
    }

    public List<ProductDto> getAllProducts() {
        return getProductQuery()
                .fetch();
    }
}
