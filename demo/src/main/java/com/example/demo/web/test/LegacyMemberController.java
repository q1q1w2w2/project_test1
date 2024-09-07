package com.example.demo.web.test;

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
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

//@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class LegacyMemberController {

    private final MemberService memberService;
    private final LoginService loginService;

    @GetMapping("/join")
    public String join(@ModelAttribute("member") MemberJoinDto member) {
        return "member/join";
    }

    @PostMapping("/join")
    public String joinMember(@Validated @ModelAttribute("member") MemberJoinDto form, BindingResult bindingResult) {
        log.info("*** join post ***");
        log.info("form = {}", form);
        log.info("bindingResult = {}", bindingResult);

        if (bindingResult.hasErrors()) {
            return "member/join";
        }
        if (loginService.isLoginIdDuplicated(form.getLoginId())) {
            bindingResult.reject("loginIdDuplicated", "이미 존재하는 ID 입니다.");
            return "member/join";
        }

        memberService.join(form);

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(@ModelAttribute("member") MemberLoginDto member) {
        return "member/login";
    }

    @PostMapping("/login")
    public String loginMember(@Validated @ModelAttribute("member") MemberLoginDto form, BindingResult bindingResult, HttpServletRequest request) {
        log.info("*** login post ***");
        log.info("form = {}", form);
        log.info("bindingResult = {}", bindingResult);

        if (bindingResult.hasErrors()) {
            return "member/login";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        log.info("loginMember = {}", loginMember);

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 틀렸습니다.");
            return "member/login";
        }

        HttpSession session = request.getSession();
        session.setAttribute("member", loginMember);

        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        log.info("*** logout post ***");

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return "redirect:/";
    }

//    @GetMapping("/test")
//    public ResponseEntity<TestRequest> test() {
//        TestRequest body = new TestRequest(HttpStatus.OK, "message", "testData");
//        return ResponseEntity.status(HttpStatus.OK).body(body);
//    }

}
