package com.sparta.msa_exam.order;

import com.sparta.msa_exam.order.core.dto.OrderRequestDto;
import com.sparta.msa_exam.order.core.dto.OrderResponseDto;
import com.sparta.msa_exam.order.core.dto.OrderSearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {


    private final OrderService orderService;

    @PostMapping
    public OrderResponseDto createOrder(
            @RequestBody OrderRequestDto orderRequestDto,
            @RequestHeader(value = "X-User-Id", required = true) String user_id) {

        return orderService.createOrder(orderRequestDto, user_id);
    }


    @GetMapping
    public Page<OrderResponseDto> getOrders(
            OrderSearchDto searchDto, Pageable pageable,
            @RequestHeader(value = "X-User-Id", required = true) String user_id) {

        return orderService.getOrders(searchDto, pageable, user_id);
    }

    @GetMapping("/{order_id}")
    public OrderResponseDto getOrderById(@PathVariable Long order_id) {
        return orderService.getOrderById(order_id);
    }

    @PutMapping("/{order_id}")
    public OrderResponseDto updateOrder(@PathVariable Long order_id,
                                        @RequestBody OrderRequestDto orderRequestDto,
                                        @RequestHeader(value = "X-User-Id", required = true) String user_id) {
        return orderService.updateOrder(order_id, orderRequestDto, user_id);
    }
}
