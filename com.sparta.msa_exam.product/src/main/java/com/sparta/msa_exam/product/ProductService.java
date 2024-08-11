package com.sparta.msa_exam.product;

import com.sparta.msa_exam.product.dto.ProductRequestDto;
import com.sparta.msa_exam.product.dto.ProductResponseDto;
import com.sparta.msa_exam.product.dto.ProductSearchDto;
import com.sparta.msa_exam.product.entity.Product;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ProductService {


    private final ProductRepository productRepository;


    // 상품 생성
    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto requestDto, String user_id){

        Product product = Product.createProduct(requestDto, user_id);
        Product savedProduct = productRepository.save(product);
        return toResponseDto(savedProduct);
    }

    // 상품 조회
    public Page<ProductResponseDto> getProducts(ProductSearchDto searchDto, Pageable pageable){
        return productRepository.searchProducts(searchDto, pageable);
    }


    @Transactional
    public ProductResponseDto getProductById(Long product_id){

        Product product = productRepository.findById(product_id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 상품을 찾을 수 없습니다."));

        return toResponseDto(product);
    }


    // 상품 수량 감소
    @Transactional
    public void reduceProductQuantity(Long product_id, int quantity) {

        Product product = productRepository.findById(product_id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + product_id));

        // 실제 상품 수량이 감소하려는 수량보다 적을 때
        if(product.getQuantity() < quantity) {
            throw new IllegalArgumentException("Not enough quantity for product ID: " + product_id);
        }

        product.reduceQuantity(quantity);
        productRepository.save(product);
    }



    // DTO 변환 메서드
    private ProductResponseDto toResponseDto(Product product){

        return new ProductResponseDto(
                product.getProduct_id(),
                product.getName(),
                product.getSupply_price(),
                product.getQuantity(),
                product.getCreatedAt(),
                product.getCreatedBy()
        );
    }
}
