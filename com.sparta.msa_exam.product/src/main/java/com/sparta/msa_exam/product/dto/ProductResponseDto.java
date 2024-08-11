package com.sparta.msa_exam.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto {

    private Long product_id;
    private String name;
    private Integer supply_price;
    private Integer quantity;
    private LocalDateTime createdAt;
    private String createdBy;
}
