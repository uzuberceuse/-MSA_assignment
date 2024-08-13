package com.sparta.msa_exam.product;

import com.sparta.msa_exam.product.dto.ProductRequestDto;
import com.sparta.msa_exam.product.dto.ProductResponseDto;
import com.sparta.msa_exam.product.dto.ProductSearchDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {


    private final ProductService productService;
    private final String serverPort;

    public ProductController(ProductService productService, @Value("${server.port}") String serverPort) {
        this.productService = productService;
        this.serverPort = serverPort;
    }


    // 상품 추가
    @PostMapping
    public ProductResponseDto createProduct(
            @RequestBody ProductRequestDto productRequestDto,
            @RequestHeader(value = "X-User-Id", required = true) String user_id) {

        return productService.createProduct(productRequestDto, user_id);
    }

    // 여러 상품 조회
    @GetMapping
    public Page<ProductResponseDto> getProducts(ProductSearchDto searchDto, Pageable pageable) {
        return productService.getProducts(searchDto, pageable);
    }

    // 특정 상품 조회
    @GetMapping("/{product_id}")
    public ProductResponseDto getProductById(@PathVariable Long product_id) {
        return productService.getProductById(product_id);
    }

    // 상품 수량 감소
    @GetMapping("/{product_id}/reduceQuantity")
    public void reduceProductQuantity(@PathVariable Long product_id, @RequestParam int quantity) {
        productService.reduceProductQuantity(product_id, quantity);
    }

    // Response Header 에 `Server-Port` 룰 추가해주는 Generic 함수입니다.
    public <T> ResponseEntity<T> createResponse(ResponseEntity<T> response) {
        HttpHeaders headers = HttpHeaders.writableHttpHeaders(response.getHeaders()); // 인자로 받은 헤더의 정보를 수정할 수 있도록 불러옵니다.
        headers.add("Server-Port", serverPort); // Response Header 에 Server-Port 키값을 추가합니다.
        return new ResponseEntity<>(response.getBody(), headers, response.getStatusCode()); //인자로 받은 값에 수정한 헤더만 적용하여 응답합니다.
    }
}
