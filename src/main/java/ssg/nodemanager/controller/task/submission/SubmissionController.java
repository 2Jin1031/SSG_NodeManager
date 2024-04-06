package ssg.nodemanager.controller.task.submission;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ssg.nodemanager.domain.Member;
import ssg.nodemanager.service.member.MemberService;
import ssg.nodemanager.service.task.SubmissionService;

@Controller
@RequiredArgsConstructor
public class SubmissionController {

    private final SubmissionService submissionService;
    private final MemberService memberService;

    //과제제출란
    @GetMapping("/task/submission")
    public String submissionCheck(HttpServletRequest request,
                                  Model model) {
        // 세션에서 로그인된 사용자 id 가져오기
        Long memberId = (Long) request.getSession().getAttribute("loggedInMemberId");

        // 로그인 id가 없으면 로그인 페이지로 리다이렉트
        if (memberId == null) {
            return "redirect:/login";
        }

        Member currentMember = memberService.findById(memberId); // 데이터베이스에서 최신 Member 정보 조회

        if (!submissionService.checkIfTaskExists(currentMember)) { // task가 존재하지 않거나 제출되지 않으면 submissionForm 페이지로 이동
            System.out.println("in task가 존재하지 않거나 제출할당 되지 않으면의 if문에 들어왔습니다");

            SubmissionForm form = new SubmissionForm();
            form.setCurrentLevel(currentMember.getCurrentLevel());
            model.addAttribute("form", form);
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
        // 세션에서 로그인된 사용자 id 가져오기
        Long memberId = (Long) request.getSession().getAttribute("loggedInMemberId");

        // 로그인 id가 없으면 로그인 페이지로 리다이렉트
        if (memberId == null) {
            return "redirect:/login";
        }

        Member currentMember = memberService.findById(memberId); // 데이터베이스에서 최신 Member 정보 조회

        submissionService.submit(currentMember, submissionUrl);
        SubmissionInfo info = submissionService.makeInfo(currentMember);
        request.getSession().setAttribute("info", info);

        return "redirect:/task/submission/complete";
    }

    @GetMapping("task/submission/complete")
    public String submissionDone(HttpServletRequest request,
                                 Model model) {
        // 세션에서 로그인된 사용자 id 가져오기
        Long memberId = (Long) request.getSession().getAttribute("loggedInMemberId");

        // 로그인 id가 없으면 로그인 페이지로 리다이렉트
        if (memberId == null) {
            return "redirect:/login";
        }

        Member currentMember = memberService.findById(memberId); // 데이터베이스에서 최신 Member 정보 조회

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
