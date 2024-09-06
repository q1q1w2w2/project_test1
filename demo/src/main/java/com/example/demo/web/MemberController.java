package com.example.demo.web;

import com.example.demo.domain.Member;
import com.example.demo.dto.MemberJoinDto;
import com.example.demo.dto.MemberLoginDto;
import com.example.demo.dto.validation.ValidationSequence;
import com.example.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

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

        memberService.join(new Member(form.getName(), form.getLoginId(), form.getPassword()));

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(@ModelAttribute("member") MemberLoginDto member) {
        return "member/login";
    }

    @PostMapping("/login")
    public String loginMember(@ModelAttribute("member") MemberLoginDto form, BindingResult bindingResult) {
        log.info("*** login post ***");
        log.info("form = {}", form);
        log.info("bindingResult = {}", bindingResult);

        if (bindingResult.hasErrors()) {
            return "member/login";
        }

        memberService.findByLoginId(form.getLoginId());

        return "redirect:/";
    }

}
