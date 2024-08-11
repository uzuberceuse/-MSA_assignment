package com.sparta.msa_exam.order.core.dto;

import com.sparta.msa_exam.order.core.client.ProductResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {

    private List<Long> product_ids;
    private String status;
}
