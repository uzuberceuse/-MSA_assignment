package com.sparta.msa_exam.auth;

import com.sparta.msa_exam.auth.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

// 로그인, 회원가입
@Service
public class AuthService {

    // application.yml 설정값
    @Value("${spring.application.name}")
    private String issuer;

    @Value("${service.jwt.access-expiration}")
    private Long accessExpiration;

    private final SecretKey secretKey;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * AuthService 생성자
     * Base64 URL 인코딩된 비밀 키를 디코딩하여 HMAC-SHA 알고리즘에 적합한 SecretKey 객체를 생성
     * @param secretKey Base64 URL 인코딩된 비밀 키
     */
    public AuthService(@Value("${service.jwt.secret-key}") String secretKey,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 사용자 ID를 받아 JWT access 토큰을 생성
     * @param user_id 사용자 ID
     * @return 생성된 JWT access 토큰
     */
    public String createAccessToken(String user_id) {
        return Jwts.builder()
                // 사용자 ID를 클레임으로 설정
                .claim("user_id", user_id)
                // JWT 발행자를 설정
                .issuer(issuer)
                // JWT 발행 시간을 현재 시간으로 설정
                .issuedAt(new Date(System.currentTimeMillis()))
                // JWT 만료 시간을 설정
                .expiration(new Date(System.currentTimeMillis() + accessExpiration))
                // SecretKey를 사용하여 HMAC-SHA512 알고리즘으로 서명
                .signWith(secretKey, io.jsonwebtoken.SignatureAlgorithm.HS512)
                // JWT 문자열로 컴팩트하게 변환
                .compact();
    }

    /**
     * 사용자 등록
     * @param user 사용자 정보
     * @return 저장된 사용자
     */
    public User signUp(User user) {
        // 패스워드 암호화
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // DB에 암호화된 값 저장
        return userRepository.save(user);
    }

    /**
     * 사용자 인증
     * @param user_id 사용자 ID
     * @param password 비밀번호
     * @return JWT 액세스 토큰
     */
    public String signIn(String user_id, String password) {
        // ID 먼저 조회
        // 패스워드까지 일치하면 토큰발행
        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid user ID or password");
        }

        return createAccessToken(user.getUser_id());
    }
}