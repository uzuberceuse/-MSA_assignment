package com.sparta.msa_exam.order;

import com.sparta.msa_exam.order.core.dto.OrderRequestDto;
import com.sparta.msa_exam.order.core.dto.OrderResponseDto;
import com.sparta.msa_exam.order.core.dto.OrderSearchDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {


    private final OrderService orderService;
    private final String serverPort;


    public OrderController(OrderService orderService, @Value("${server.port}") String serverPort) {
        this.orderService = orderService;
        this.serverPort = serverPort;
    }


    //주문 추가
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(
            @RequestBody OrderRequestDto orderRequestDto,
            @RequestHeader(value = "X-User-Id", required = true) String user_id) {

        return createResponse(ResponseEntity.ok(orderService.createOrder(orderRequestDto, user_id)));
    }


    @GetMapping
    public Page<OrderResponseDto> getOrders(
            OrderSearchDto searchDto, Pageable pageable,
            @RequestHeader(value = "X-User-Id", required = true) String user_id) {

        return orderService.getOrders(searchDto, pageable, user_id);
    }


    // 주문 단건 조회
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable(name="orderId") Long order_id) {
        return createResponse(ResponseEntity.ok(orderService.getOrderById(order_id)));
    }


    // 주문 수정
    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> updateOrder(@PathVariable(name="orderId")  Long order_id,
                                        @RequestBody OrderRequestDto orderRequestDto,
                                        @RequestHeader(value = "X-User-Id", required = true) String user_id) {
        return createResponse(ResponseEntity.ok(orderService.updateOrder(order_id, orderRequestDto, user_id)));
    }


    // Response Header 에 `Server-Port` 룰 추가해주는 Generic 함수입니다.
    public <T> ResponseEntity<T> createResponse(ResponseEntity<T> response) {
        HttpHeaders headers = HttpHeaders.writableHttpHeaders(response.getHeaders()); // 인자로 받은 헤더의 정보를 수정할 수 있도록 불러옵니다.
        headers.add("Server-Port", serverPort); // Response Header 에 Server-Port 키값을 추가합니다.
        return new ResponseEntity<>(response.getBody(), headers, response.getStatusCode()); //인자로 받은 값에 수정한 헤더만 적용하여 응답합니다.
    }
}
