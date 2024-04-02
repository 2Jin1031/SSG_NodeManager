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
    public String login(@RequestParam("loginId") String loginId, HttpServletRequest request, Model model) {
        // loginId의 유효성 검사
        if (!StringUtils.hasText(loginId)) {
            model.addAttribute("loginError", "Login ID is required");
            return "members/loginForm";
        }

        Member member = memberService.findByLoginId(loginId);
        if (member != null) {
            // 로그인 성공: 세션에 사용자 정보 저장
            request.getSession().setAttribute("loggedInMember", member);
            return "members/profile";
        } else {
            // 로그인 실패: 에러 메시지 설정
            model.addAttribute("loginError", "Invalid login ID");
            return "members/loginForm";
        }
    }

    // 개인페이지
    @GetMapping("/members")
    public String profile() {
        return "member/profile";
    }
}
