package com.example.demo.config;

import com.example.demo.exception.CustomAuthenticationException;
import com.example.demo.jwt.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity // spring security 필터가 필터체인에 등록
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true) // @PreAuthorize 사용을 위함
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                // 예외 처리
                .exceptionHandling(exceptionHandling ->
                    exceptionHandling
                            .accessDeniedHandler(jwtAccessDeniedHandler)
                            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )

                // 인증 없이 접근 가능
                .authorizeHttpRequests(authorizeRequests ->
                    authorizeRequests
                            .requestMatchers("/member/login", "/member/join","/", "/test/**").permitAll()
                            .requestMatchers(PathRequest.toH2Console()).permitAll()
                            .anyRequest().authenticated()
                )

                // 세션 X, STATELESS
                .sessionManagement((session) ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // X-Frame-Options 헤더를 sameOrigin 으로 설정(클릭재킹 방지)
                // 클릭재킹 -> 투명한 iframe을 사용하여 악의적인 버튼을 클릭하게 함
                .headers(headers ->
                        headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )

                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);

//                .with(new JwtSecurityConfig(tokenProvider), customizer -> {
//
//                })

//                .addFilterBefore(new CustomAuthenticationException(authenticationManager()), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
