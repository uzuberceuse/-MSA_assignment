package com.sparta.msa_exam.product.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDto {

    private String name;
    private Integer supply_price;
    private Integer quantity;
}
