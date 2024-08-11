package com.sparta.msa_exam.gateway;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;


// JWT 토큰 인증 필터
@Slf4j
@Component
public class LocalJwtAuthenticationFilter implements GlobalFilter {

    // application.yml 설정값
    @Value("${service.jwt.secret-key}")
    private String secretKey;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        // /signIn 경로는 필터를 적용하지 않음
        if (path.equals("/auth/signIn")) {
            return chain.filter(exchange);
        }

        // 아래 extractToken 메서드 참고
        // 토큰값 추출
        String token = extractToken(exchange);

        // 아래 validateToken 메서드 참고
        if (token == null || !validateToken(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    // 토큰 추출 메서드
    private String extractToken(ServerWebExchange exchange) {

        // 요청 헤더가 Authorization일 때 값 가져옴
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    // 토큰 유효 검증
    private boolean validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
            Jws<Claims> claimsJws = Jwts.parser()
                    .verifyWith(key)
                    .build().parseSignedClaims(token);

            // 토큰 값 로깅
            log.info("#####payload :: " + claimsJws.getPayload().toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}