package com.sparta.msa_exam.auth;

import com.sparta.msa_exam.auth.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {


    private final AuthService authService;

    /**
     * 사용자 ID를 받아 JWT access 토큰을 생성하여 응답
     * @RequestBody 로그인 요청 객체
     * @return JWT access 토큰을 포함한 AuthResponse 객체를 반환
     */
    @PostMapping("/signIn")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody SignInRequest signInRequest){
        String token = authService.signIn(signInRequest.getUser_id(), signInRequest.getPassword());
        return ResponseEntity.ok(new AuthResponse(token));
    }


    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody User user) {
        User createdUser = authService.signUp(user);
        return ResponseEntity.ok(createdUser);
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