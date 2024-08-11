package com.sparta.msa_exam.order.core.enums;

// 권한이나 상태 저장할 때 ENUM 사용
// 보통 DB에 숫자로 넣으면 성능이 좋아지지만 가독성을 위해 String으로 함
public enum OrderStatus {
    CREATED, PAID, SHIPPED, COMPLETED, CANCELLED
}