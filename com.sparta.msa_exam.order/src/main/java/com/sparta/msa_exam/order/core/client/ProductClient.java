package com.sparta.msa_exam.order.core.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

// product-service를 호출
@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/products/{product_id}")
    ProductResponseDto getProduct(@PathVariable("product_id") Long product_id);

    @GetMapping("/products/{product_id}/reduceQuantity")
    void reduceProductQuantity(@PathVariable("product_id") Long product_id, @RequestParam Integer quantity);

}
