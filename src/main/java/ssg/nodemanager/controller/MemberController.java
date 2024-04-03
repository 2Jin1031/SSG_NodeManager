package ssg.nodemanager.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ssg.nodemanager.service.MemberService;
import ssg.nodemanager.domain.Member;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 로그인
    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "members/loginForm";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@ModelAttribute("loginForm") LoginForm form,
                        HttpServletRequest request,
                        Model model) {

        // loginId의 유효성 검사
        if (!StringUtils.hasText(form.getLoginId())) {
            model.addAttribute("loginError", "Login ID is required");
            return "members/loginForm";
        }

        try {
            memberService.signIn(form);

            Member member = memberService.findByLoginId(form.getLoginId());
            request.getSession().setAttribute("loggedInMember", member);
            return "members/profile";
        } catch (IllegalStateException e) {
            model.addAttribute("loginError2", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "members/loginForm";
        }
    }

    // 개인페이지
    @GetMapping("/members")
    public String profile() {
        return "member/profile";
    }

    // 개인 정보 수정 페이지
    @GetMapping("/members/modify")
    public String infoModify() {
        return "members/infoModification";
    }
}
