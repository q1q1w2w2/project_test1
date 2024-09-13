package com.example.jwt2.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@Slf4j
public class SecurityUtil {

    private SecurityUtil() {

    }

    public static Optional<String> getCurrentUsername() {
        // SecurityContextHolder에서 인증 정보 가져오기
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            log.debug("Security Context에 인증 정보가 없음");
            return Optional.empty();
        }

        // 인증 객체의 principal(주체)가 UserDetails의 인스턴스라면, 사용자 이름 가져오기
        // 문자열이라면 그대로 사용
        String username = null;
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            username = springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof String) {
            username = (String) authentication.getPrincipal();
        }

        // username이 null이라면 Optional.empty(), 아니면 Optional로 감싸서 반환
        return Optional.ofNullable(username);
    }
}
