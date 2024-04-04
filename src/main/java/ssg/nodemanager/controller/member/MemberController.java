package ssg.nodemanager.controller.member;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ssg.nodemanager.domain.Member;
import ssg.nodemanager.service.member.MemberService;

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
            return "redirect:/login";
        }

        try {
            // 아이디, 비번 검사
            memberService.signIn(form);

            Member member = memberService.findByLoginId(form.getLoginId());
            request.getSession().setAttribute("loggedInMember", member);
            return "redirect:/members";
        } catch (IllegalStateException e) {
            model.addAttribute("loginErrorByIdWithPassword", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "redirect:/login";
        }
    }

    // 개인 페이지
    @GetMapping("/members")
    public String profile() {
        return "members/profile";
    }

    // 개인 정보 수정 페이지
    @GetMapping("/members/modify")
    public String infoModify() {
        return "members/infoModification";
    }
}
