package com.sparta.msa_exam.auth;

import com.sparta.msa_exam.auth.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthController {


    private final AuthService authService;
    private final String serverPort;


    public AuthController(AuthService authService, @Value("${server.port}") String serverPort) {
        this.authService = authService;
        this.serverPort = serverPort;
    }


    /**
     * 사용자 ID를 받아 JWT access 토큰을 생성하여 응답
     * @return JWT access 토큰을 포함한 AuthResponse 객체를 반환
     */
    @GetMapping("/signIn")
    public ResponseEntity<?> createAuthenticationToken(@RequestParam String user_id,@RequestParam String password) {
        String token = authService.signIn(user_id, password);
        return createResponse(ResponseEntity.ok(new AuthResponse(token)));
    }


    // 회원가입
    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody User user) {
        User createdUser = authService.signUp(user);
        return ResponseEntity.ok(createdUser);
    }


    // Response Header 에 `Server-Port` 룰 추가
    public <T> ResponseEntity<T> createResponse(ResponseEntity<T> response) {
        // 인자로 받은 헤더의 정보를 수정할 수 있도록 불러옴
        HttpHeaders headers = HttpHeaders.writableHttpHeaders(response.getHeaders());
        // Response Header 에 Server-Port 키값 추가
        headers.add("Server-Port", serverPort);
        return new ResponseEntity<>(response.getBody(), headers, response.getStatusCode());
    }


     // JWT access 토큰을 포함하는 응답 객체
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class AuthResponse {
        private String access_token;
    }


    // 로그인 요청 객체 생성
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class SignInRequest {
        private String user_id;
        private String password;
    }
}