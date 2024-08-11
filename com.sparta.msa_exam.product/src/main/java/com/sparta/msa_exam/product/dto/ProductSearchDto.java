package com.sparta.msa_exam.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchDto {

    private String name;
    private Double minPrice;
    private Double maxPrice;

}
