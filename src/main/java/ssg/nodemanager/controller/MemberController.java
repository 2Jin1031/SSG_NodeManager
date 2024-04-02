package ssg.nodemanager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ssg.nodemanager.MemberService;
import ssg.nodemanager.domain.Member;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "members/loginForm";
    }

    @PostMapping("/login")
    public String login(@Valid LoginForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "member/loginForm";
        }

        Member member = new Member(form.getMemberId(), form.getPassword());

        memberService.signIn(member);
        return "redirect:/";
    }
}
