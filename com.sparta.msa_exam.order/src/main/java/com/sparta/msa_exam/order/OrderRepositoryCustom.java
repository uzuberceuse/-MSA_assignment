package com.sparta.msa_exam.order;

import com.sparta.msa_exam.order.core.dto.OrderResponseDto;
import com.sparta.msa_exam.order.core.dto.OrderSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {
    Page<OrderResponseDto> searchOrders(OrderSearchDto searchDto, Pageable pageable, String user_id);
}
