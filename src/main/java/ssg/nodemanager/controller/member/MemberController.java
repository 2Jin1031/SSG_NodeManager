package ssg.nodemanager.controller.member;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ssg.nodemanager.domain.Member;
import ssg.nodemanager.service.RankService;
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
            Member member = memberService.signIn(form);

            request.getSession().setAttribute("loggedInMemberId", member.getId());
            return "redirect:/members";
        } catch (IllegalStateException e) {
            model.addAttribute("loginErrorByIdWithPassword", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "redirect:/login";
        }
    }

    // 개인 페이지
    @GetMapping("/members")
    public String profile(Model model) {
        int maxLevel = memberService.getMaxLevel();
        int minLevel = memberService.getMinLevel();
        model.addAttribute("maxLevel", maxLevel);
        model.addAttribute("minLevel", minLevel);
        return "members/profile";
    }

    // 개인 정보 수정 페이지
    @GetMapping("/members/modify")
    public String infoModify(HttpServletRequest request, Model model) {
        Long memberId = (Long) request.getSession().getAttribute("loggedInMemberId");
        if (memberId == null) {
            return "redirect:/login";
        }

        Member member = memberService.findById(memberId);
        model.addAttribute("member", member);
        model.addAttribute("canChangeId", !member.isHasChangedLoginId());
        model.addAttribute("canChangePassword", !member.isHasChangedPassword());
        return "members/infoModification";
    }

    // 아이디 변경 처리
    @PostMapping("/members/modify/id")
    public String updateLoginId(@RequestParam("newLoginId") String newLoginId,
                                HttpServletRequest request,
                                RedirectAttributes redirectAttributes) {
        Long memberId = (Long) request.getSession().getAttribute("loggedInMemberId");
        if (memberId == null) {
            redirectAttributes.addFlashAttribute("updateError", "로그인이 필요합니다.");
            return "redirect:/login";
        }

        try {
            memberService.updateLoginId(memberId, newLoginId);
            redirectAttributes.addFlashAttribute("updateSuccess", "아이디가 성공적으로 변경되었습니다.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("updateError", e.getMessage());
        }
        return "redirect:/members";
    }

    // 비밀번호 변경 처리
    @PostMapping("/members/modify/password")
    public String updatePassword(@RequestParam("newPassword") String newPassword,
                                HttpServletRequest request,
                                RedirectAttributes redirectAttributes) {
        Long memberId = (Long) request.getSession().getAttribute("loggedInMemberId");
        if (memberId == null) {
            redirectAttributes.addFlashAttribute("updateError", "로그인이 필요합니다.");
            return "redirect:/login";
        }

        try {
            memberService.updatePassword(memberId, newPassword);
            redirectAttributes.addFlashAttribute("updateSuccess", "비밀번호가 성공적으로 변경되었습니다.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("updateError", e.getMessage());
        }
        return "redirect:/members";
    }
}
