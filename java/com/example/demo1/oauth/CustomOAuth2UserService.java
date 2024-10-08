package com.example.demo1.oauth;

import com.example.demo1.domain.User;
import com.example.demo1.dto.JoinDto;
import com.example.demo1.dto.UpdateDto;
import com.example.demo1.repository.UserRepository;
import com.example.demo1.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    // OAuth 인증을 통해 사용자 정보를 가져오는 역할
    // OAuth2 로그인 성공 시 DB에 저장

    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("[CustomOAuth2UserService 실행] - loadUser");

        // 기본 OAuth2UserService 객체 생성
        OAuth2UserService<OAuth2UserRequest, OAuth2User> service = new DefaultOAuth2UserService();

        // OAuth2UserService를 사용하여 OAuth2User 정보를 가져온다.
        OAuth2User oAuth2User = service.loadUser(userRequest);
        // OAuth2User의 속성(name, picture, email, email_verified 정보 담겨있음)
        Map<String, Object> originAttributes = oAuth2User.getAttributes();

        // 클라이언트 등록 ID(google, kakao, naver)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // OAuth2Attribute: OAuth2User의 attribute를 서비스 유형에 맞게 담아주는 클래스
        OAuth2Attribute attribute =
                OAuth2Attribute.of(registrationId, originAttributes);

        User user = saveOrUpdate(attribute);
        String loginId = user.getLoginId();

        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        return new OAuth2CustomUser(registrationId, originAttributes, authorities, loginId);
    }

    private User saveOrUpdate(OAuth2Attribute attribute) {
//        User user = userRepository.findByLoginId(attribute.getEmail()) // 기존 회원에 이메일 없음 -> 이메일을 LoginId로 사용
//                .map(entity -> entity.updateUsername(attribute.getName())) // 이미 존재하는 회원(LoginId로 이메일이 있다면) 업데이트
//                .orElse(attribute.toEntity()); // 새로운 회원이면 새 유저 생성
//        return userRepository.save(user);

        Optional<User> findUser = userRepository.findByLoginId(attribute.getEmail());
        if (findUser.isPresent()) {
            // 업데이트
            userService.update(findUser.get().getId(), new UpdateDto("password", "tel", "address", "detail")); // 임시
            return findUser.get();
        }
        // 새로 생성
        return userService.join(new JoinDto(attribute.getName(), attribute.getEmail(), "password", LocalDate.now(), "tel", "address", "detail"));
    }
}
