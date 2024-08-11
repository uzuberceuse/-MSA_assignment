package com.sparta.msa_exam.order.core.dto;

import com.sparta.msa_exam.order.core.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter
@Setter
public class OrderSearchDto {

    private OrderStatus status;
    private List<Long> product_ids;
    private String sortBy;
    private Pageable pageable;
}