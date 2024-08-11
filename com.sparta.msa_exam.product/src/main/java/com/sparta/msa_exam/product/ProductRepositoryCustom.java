package com.sparta.msa_exam.product;

import com.sparta.msa_exam.product.dto.ProductResponseDto;
import com.sparta.msa_exam.product.dto.ProductSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

    // 상품 조회 시 페이징 적용
    Page<ProductResponseDto> searchProducts(ProductSearchDto searchDto, Pageable pageable);
}
