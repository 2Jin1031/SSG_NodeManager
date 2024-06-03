package ssg.nodemanager.controller.task.submission;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ssg.nodemanager.domain.Member;
import ssg.nodemanager.service.member.MemberService;
import ssg.nodemanager.service.task.SubmissionService;
import ssg.nodemanager.service.task.XssService;

@Controller
@RequiredArgsConstructor
public class SubmissionController {

    private final SubmissionService submissionService;
    private final MemberService memberService;
    private final XssService xssService;

    //과제제출란
    @GetMapping("/task/submission")
    public String submissionCheck(HttpServletRequest request,
                                  Model model) {
        // 세션에서 Member ID를 가져옴
        Long memberId = (Long) request.getSession().getAttribute("loggedInMemberId");
        if (memberId == null) {
            // 로그 출력
            System.out.println("Member ID is null. User might not be logged in.");
            return "redirect:/login";  // 로그인 페이지로 리다이렉트
        }

        // Member 객체 조회
        Member currentMember = memberService.findById(memberId);
        if (currentMember == null) {
            // 로그 출력
            System.out.println("Member not found for ID: " + memberId);
            return "redirect:/login";  // 로그인 페이지로 리다이렉트
        }

        request.getSession().setAttribute("currentMember", currentMember);

        if (!submissionService.hasTask(currentMember)) { // task가 존재하지 않거나 제출되지 않으면 submissionForm 페이지로 이동
            SubmissionForm form = new SubmissionForm();
            form.setCurrentLevel(currentMember.getCurrentLevel());
            model.addAttribute("form", form);

            if (xssService.isLevel(currentMember.getCurrentLevel())) {
                model.addAttribute("submittedContents", xssService.getSubmittedContents());
            }

            return "task/submissionForm";
        }

        // task가 존재하면 /task/submission/complete 페이지로 리다이렉트
        SubmissionInfo info = submissionService.makeInfo(currentMember);
        request.getSession().setAttribute("info", info);

        return "redirect:/task/submission/complete";

    }

    @PostMapping("/task/submission")
    public String submit(@RequestParam("url") String submissionUrl,
                         HttpServletRequest request) {
        // 세션에서 Member ID를 가져옴
        Long memberId = (Long) request.getSession().getAttribute("loggedInMemberId");
        if (memberId == null) {
            // 로그 출력
            System.out.println("Member ID is null. User might not be logged in.");
            return "redirect:/login";  // 로그인 페이지로 리다이렉트
        }

        // Member 객체 조회
        Member currentMember = memberService.findById(memberId);
        if (currentMember == null) {
            // 로그 출력
            System.out.println("Member not found for ID: " + memberId);
            return "redirect:/login";  // 로그인 페이지로 리다이렉트
        }

        submissionService.submit(currentMember, submissionUrl);
        SubmissionInfo info = submissionService.makeInfo(currentMember);
        request.getSession().setAttribute("info", info);

        return "redirect:/task/submission/complete";
    }

    @GetMapping("task/submission/complete")
    public String submissionDone(HttpServletRequest request,
                                 Model model) {
        Member currentMember = (Member) request.getAttribute("currentMember");

        // 세션에서 SubmissionInfo 객체를 가져옵니다.
        SubmissionInfo info = (SubmissionInfo) request.getSession().getAttribute("info");
        if (info == null) {
            // 세션에 정보가 없는 경우, 데이터베이스에서 조회
            info = submissionService.makeInfo(currentMember);
        }
        model.addAttribute("info", info);
        return "task/submissionDone";
    }
}
