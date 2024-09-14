package com.example.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@Slf4j
public class SecurityUtil {

    private SecurityUtil() {

    }

    // security Context의 authentication 객체를 이용하여 loginId 리턴
    public static Optional<String> getCurrentUsername() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("authentication: {}", authentication);

        if (authentication == null) {
            log.error("Security Context 에 인증 정보가 없습니다.");
            return Optional.empty();
        }

        String loginId = null;
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails springSecurityUser = ((UserDetails) authentication.getPrincipal());
            loginId = springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof String) {
            loginId = (String) authentication.getPrincipal();
        }

        return Optional.ofNullable(loginId);
    }
}
