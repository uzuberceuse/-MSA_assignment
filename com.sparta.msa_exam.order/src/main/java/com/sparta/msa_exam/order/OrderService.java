package com.sparta.msa_exam.order;

import com.sparta.msa_exam.order.core.client.ProductClient;
import com.sparta.msa_exam.order.core.client.ProductResponseDto;
import com.sparta.msa_exam.order.core.dto.OrderRequestDto;
import com.sparta.msa_exam.order.core.dto.OrderResponseDto;
import com.sparta.msa_exam.order.core.dto.OrderSearchDto;
import com.sparta.msa_exam.order.core.entity.Order;
import com.sparta.msa_exam.order.core.enums.OrderStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;


    // 상품주문
    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto requestDto, String user_id) {

        // 구매 가능 상품인지 수량 체크
        for(Long product_id : requestDto.getProduct_ids()){
            ProductResponseDto product = productClient.getProduct(product_id);
            log.info("########## 상품 수량 확인 : " + product.getQuantity());
            if(product.getQuantity() < 1){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "상품 아이디: " + product_id + "현재 재고 없음.");
            }
        }

        // 상품 수량 1 감소
        for(Long product_id : requestDto.getProduct_ids()){
            productClient.reduceProductQuantity(product_id, 1);
        }

        // 주문 생성하기
        Order order = Order.createOrder(requestDto.getProduct_ids(), user_id);
        Order savedOrder = orderRepository.save(order);
        return toResponseDto(savedOrder);
    }

    // 주문 건수  조회
    public Page<OrderResponseDto> getOrders(OrderSearchDto searchDto, Pageable pageable, String user_id) {
        return orderRepository.searchOrders(searchDto, pageable, user_id);
    }


    // 특정 주문 조회
    @Transactional
    public OrderResponseDto getOrderById(Long order_id) {

        Order order = orderRepository.findById(order_id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "주문 내역을 찾을 수 없습니다."));

        return toResponseDto(order);
    }

    @Transactional
    public OrderResponseDto updateOrder(Long order_id, OrderRequestDto requestDto, String user_id) {
        Order order = orderRepository.findById(order_id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "주문 내역을 찾을 수 없습니다."));

        order.updateOrder(requestDto.getProduct_ids(), user_id, OrderStatus.valueOf(requestDto.getStatus()));
        Order updatedOrder = orderRepository.save(order);

        return toResponseDto(updatedOrder);
    }


    private OrderResponseDto toResponseDto(Order order) {
        return new OrderResponseDto(
                order.getOrder_id(),
                order.getStatus().name(),
                order.getCreatedAt(),
                order.getCreatedBy(),
                order.getUpdatedAt(),
                order.getUpdatedBy(),
                order.getProduct_ids()
        );
    }
}
