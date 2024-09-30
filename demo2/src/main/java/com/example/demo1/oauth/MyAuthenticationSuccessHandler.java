package com.example.demo1.oauth;

import com.example.demo1.jwt.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class MyAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    // OAuth 인증 성공 후 처리를 위한 클래스

    private final TokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("[onAuthenticationSuccess 시작]");

        // OAuth2User로 캐스팅하여 인증된 사용자 정보를 가져온다.
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        // 사용자 이메일을 가져온다.
        String email = oAuth2User.getAttribute("email");
        // 서비스 제공 플랫폼(GOOGLE, KAKAO, NAVER)이 어디인지 가져온다.
        String provider = oAuth2User.getAttribute("provider");

        // CustomOAuth2UserService에서 셋팅한 로그인한 회원 존재 여부를 가져온다.
        boolean isExist = oAuth2User.getAttribute("exist");
        // OAuth2User로 부터 Role을 얻어온다.
        String role = oAuth2User.getAuthorities().stream().
                findFirst() // 첫번째 Role을 찾아온다.
                .orElseThrow(IllegalAccessError::new) // 존재하지 않을 시 예외를 던진다.
                .getAuthority(); // Role을 가져온다.

        // 회원이 존재할경우
        if (isExist) {
            // 회원이 존재하면 jwt token 발행을 시작한다.
            String accessToken = null;
            String refreshToken = null;
            try {
                accessToken = tokenProvider.createAccessToken(email, "ROLE_" + role);
                refreshToken = tokenProvider.createRefreshToken(email);
            } catch (Exception e) {
                throw new RuntimeException("토큰 생성 실패");
            }

            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("accessToken", accessToken);
            responseMap.put("refreshToken", refreshToken);

            ObjectMapper objectMapper = new ObjectMapper();
            response.setContentType("application/json");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            objectMapper.writeValue(response.getOutputStream(), responseMap);

        } else {
            // 회원이 존재하지 않을 경우, 해당 이메일로 가입?
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("error", "회원가입이 필요합니다.");

            ObjectMapper objectMapper = new ObjectMapper();
            response.setContentType("application/json");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            objectMapper.writeValue(response.getOutputStream(), responseMap);

        }
    }
}
