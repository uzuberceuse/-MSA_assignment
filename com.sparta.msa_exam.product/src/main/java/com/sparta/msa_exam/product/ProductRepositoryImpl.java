package com.sparta.msa_exam.product;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.msa_exam.product.dto.ProductResponseDto;
import com.sparta.msa_exam.product.dto.ProductSearchDto;
import com.sparta.msa_exam.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.sparta.msa_exam.product.entity.QProduct.product;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {


    private final JPAQueryFactory queryFactory;


    @Override
    public Page<ProductResponseDto> searchProducts(ProductSearchDto searchDto, Pageable pageable) {

        List<OrderSpecifier<?>> orders = getAllOrderSpecifiers(pageable);

        QueryResults<Product> results = queryFactory
                .selectFrom(product)
                // 이름, 가격 범위
                .where(
                        nameContains(searchDto.getName()),
                        priceBetween(searchDto.getMinPrice(), searchDto.getMaxPrice())
                )
                // 리스트 결과값을 정렬
                .orderBy(orders.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                // DB를 한번에 조회하면 문제 생길 수 있음
                .limit(pageable.getPageSize())
                .fetchResults();

        List<ProductResponseDto> content = results.getResults().stream()
                .map(Product::toResponseDto)
                .collect(Collectors.toList());
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }


    // searchDto에서 이름을 검색하지 않으면 따로 NULL로 반환해 무시하고 검색조건에서 빼버린다.
    private BooleanExpression nameContains(String name) {
        return name != null ? product.name.containsIgnoreCase(name) : null;
    }

    // searchDto에서 가격을 검색하지 않으면 따로 NULL로 반환해 무시하고 검색조건에서 빼버린다.
    private BooleanExpression priceBetween(Double minPrice, Double maxPrice) {
        if (minPrice != null && maxPrice != null) {
            return product.supply_price.between(minPrice, maxPrice);
        } else if (minPrice != null) {
            return product.supply_price.goe(minPrice);
        } else if (maxPrice != null) {
            return product.supply_price.loe(maxPrice);
        } else {
            return null;
        }
    }

    private List<OrderSpecifier<?>> getAllOrderSpecifiers(Pageable pageable) {

        List<OrderSpecifier<?>> orders = new ArrayList<>();

        if(pageable.getSort() != null) {
            for (Sort.Order sortOrder : pageable.getSort()) {
                com.querydsl.core.types.Order direction = sortOrder.isAscending() ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC;
                switch (sortOrder.getProperty()) {
                    case "createdAt":
                        orders.add(new OrderSpecifier<>(direction, product.createdAt));
                        break;
                    case "supply_price":
                        orders.add(new OrderSpecifier<>(direction, product.supply_price));
                        break;
                    case "quantity":
                        orders.add(new OrderSpecifier<>(direction, product.quantity));
                        break;
                    default:
                        break;
                }
            }
        }
        return orders;
    }
}
