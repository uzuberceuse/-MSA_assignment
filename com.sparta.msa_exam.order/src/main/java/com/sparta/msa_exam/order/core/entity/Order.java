package com.sparta.msa_exam.order.core.entity;

import com.sparta.msa_exam.order.core.client.ProductResponseDto;
import com.sparta.msa_exam.order.core.dto.OrderResponseDto;
import com.sparta.msa_exam.order.core.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@Table(name="order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long order_id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ElementCollection
    @CollectionTable(name = "order_items", joinColumns = @JoinColumn(name = "order_id"))
    @Column(name = "order_item_id")
    private List<Long> product_ids;

    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;


    // 주문 생성시 주문 시간을 현재 시간으로
    // 주문 상태 코드 반영
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = OrderStatus.CREATED;
        }
    }

    // 업데이트할 때 날짜를 현재 시간으로
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void updateStatus(OrderStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    // 팩토리 메서드
    public static Order createOrder(List<Long> product_ids, String createdBy) {
        return Order.builder()
                .product_ids(product_ids)
                .createdBy(createdBy)
                .status(OrderStatus.CREATED)
                .build();
    }

    // 업데이트 메서드
    public void updateOrder(List<Long> product_ids, String updatedBy, OrderStatus status) {
        this.product_ids = product_ids;
        this.updatedBy = updatedBy;
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    // DTO로 변환하는 메서드
    public OrderResponseDto toResponseDto() {
        return new OrderResponseDto(
                this.order_id,
                this.status.name(),
                this.createdAt,
                this.createdBy,
                this.updatedAt,
                this.updatedBy,
                this.product_ids
        );
    }
}
