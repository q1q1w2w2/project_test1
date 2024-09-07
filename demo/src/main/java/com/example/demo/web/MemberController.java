package com.example.demo.web;

import com.example.demo.domain.Member;
import com.example.demo.dto.MemberJoinDto;
import com.example.demo.dto.MemberLoginDto;
import com.example.demo.service.LoginService;
import com.example.demo.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final LoginService loginService;

    @GetMapping("/join")
    public String join(@ModelAttribute("member") MemberJoinDto member) {
        return "member/join";
    }

    @PostMapping("/join")
    public ResponseEntity<String> joinMember(@Validated @ModelAttribute("member") MemberJoinDto dto, BindingResult bindingResult) {
        log.info("*** join post ***");
        log.info("form = {}", dto);
        log.info("bindingResult = {}", bindingResult);

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("조건에 맞지 않습니다.");
        }
        if (loginService.isLoginIdDuplicated(dto.getLoginId())) {
            // 409 conflict 대상 리소스의 현재 상태와 충돌하여 처리할 수 없음
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 ID 입니다.");
        }

        memberService.join(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료 되었습니다.");
    }

    @GetMapping("/login")
    public String login(@ModelAttribute("member") MemberLoginDto member) {
        return "member/login";
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginMember(@Validated @ModelAttribute("member") MemberLoginDto form, BindingResult bindingResult, HttpServletRequest request) {
        log.info("*** login post ***");
        log.info("form = {}", form);
        log.info("bindingResult = {}", bindingResult);

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비어있을 수 없습니다.");
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        log.info("loginMember = {}", loginMember);

        if (loginMember == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("아이디 또는 비밀번호가 틀렸습니다.");
        }

        // 세션 생성
        HttpSession session = request.getSession();
        session.setAttribute("member", loginMember);

        return ResponseEntity.status(HttpStatus.OK).body("로그인 성공");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        log.info("*** logout post ***");

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            return ResponseEntity.status(HttpStatus.OK).body("로그아웃 성공");
        }

        return ResponseEntity.status(HttpStatus.OK).body("로그인되어있지 않습니다");
    }
}
