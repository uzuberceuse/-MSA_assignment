package com.sparta.msa_exam.product.entity;


import com.sparta.msa_exam.product.dto.ProductRequestDto;
import com.sparta.msa_exam.product.dto.ProductResponseDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED) // 외부에서 생성 못하게
@Builder(access = AccessLevel.PRIVATE)
@Table(name ="product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 생성을 데이터베이스에 위임
    private Long product_id;

    private String name;
    private Integer supply_price;
    private Integer quantity;

    private LocalDateTime createdAt;
    private String createdBy;

    // 상품 생성 시 생성 일자를 현재 시간으로
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // buildup 패턴으로 product 생성
    public static Product createProduct(ProductRequestDto requestDto, String user_id) {
        return Product.builder()
                .name(requestDto.getName())
                .supply_price(requestDto.getSupply_price())
                .quantity(requestDto.getQuantity())
                .createdBy(user_id)
                .build();
    }

    // DTO 변환 메서드
    public ProductResponseDto toResponseDto() {
        return new ProductResponseDto(
                this.product_id,
                this.name,
                this.supply_price,
                this.quantity,
                this.createdAt,
                this.createdBy
        );
    }

    // 주문한 숫자만큼 현재 수량에서 수량 제거
    public void reduceQuantity(int number) {
        this.quantity -= number;
    }
}
