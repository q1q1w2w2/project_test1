package com.example.demo1.service;

import com.example.demo1.domain.User;
import com.example.demo1.oauth.OAuth2Attribute;
import com.example.demo1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    // OAuth 인증을 통해 사용자 정보를 가져오는 역할

    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 기본 OAuth2UserService 객체 생성
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();

        // OAuth2UserService를 사용하여 OAuth2User 정보를 가져온다.
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        // 클라이언트 등록 ID(google)와 사용자 이름 속성 가져온다.
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // OAuth2UserService를 사용하여 가져온 OAuth2User 정보로 OAuth2Attribute 객체를 만든다.
        OAuth2Attribute oAuth2Attribute =
                OAuth2Attribute.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        // OAuth2Attribute의 속성값들을 Map으로 반환
        Map<String, Object> userAttribute = oAuth2Attribute.convertToMap();

        // 사용자 email(or id) 정보를 가져온다.
        String email = (String) userAttribute.get("email");
        // 이메일로 가입된 회원인지 조회한다.(기존 아이디와 비교)
        Optional<User> findUser = userService.findByLoginId(email);

        if (findUser.isEmpty()) {
            // 회원이 존재하지 않는다면, userAttribute의 exist를 false로
            userAttribute.put("exist", false);
            // 회원 권한, 회원 속성, 속성 이름을 이용해 DefaultOAuth2User 객체를 생성해 반환
            return new DefaultOAuth2User(
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                    userAttribute, "email"
            );
        }
        // 회원이 존재하면
        userAttribute.put("exist", true);
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(findUser.get().getAuthority())),
                userAttribute, "email"
        );
    }
}
