package com.sparta.msa_exam.product;

import com.sparta.msa_exam.product.dto.ProductRequestDto;
import com.sparta.msa_exam.product.dto.ProductResponseDto;
import com.sparta.msa_exam.product.dto.ProductSearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 상품 추가
    @PostMapping
    public ProductResponseDto createProduct(
            @RequestBody ProductRequestDto productRequestDto,
            @RequestHeader(value = "X-User-Id", required = true) String user_id) {

        return productService.createProduct(productRequestDto, user_id);
    }

    // 상품 조회
    @GetMapping
    public Page<ProductResponseDto> getProducts(ProductSearchDto searchDto, Pageable pageable) {
        return productService.getProducts(searchDto, pageable);
    }
}