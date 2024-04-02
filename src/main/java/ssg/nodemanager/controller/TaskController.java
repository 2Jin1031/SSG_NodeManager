package ssg.nodemanager.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ssg.nodemanager.domain.Member;
import ssg.nodemanager.domain.Task;
import ssg.nodemanager.service.MemberService;
import ssg.nodemanager.service.TaskService;

@Controller
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final MemberService memberService;

    //과제제출란
    @GetMapping("/task/submission")
    public String submissionForm(HttpServletRequest request, Model model) {
        // 세션에서 로그인된 사용자 정보 가져오기
        Member currentMember = (Member) request.getSession().getAttribute("loggedInMember");

        // 로그인된 사용자가 없으면 로그인 페이지로 리다이렉트
        if (currentMember == null) {
            return "redirect:/login";
        }

        SubmissionForm form = new SubmissionForm();
        form.setCurrentLevel(currentMember.getCurrentLevel());

        model.addAttribute("form", form);
        return "task/submissionForm";
    }

    @PostMapping("/task/submission")
    public String submit(@RequestParam("url") String submissionUrl, HttpServletRequest request, Model model) {
        // 세션에서 로그인된 사용자 정보 가져오기
        Member currentMember = (Member) request.getSession().getAttribute("loggedInMember");

        // 로그인된 사용자가 없으면 로그인 페이지로 리다이렉트
        if (currentMember == null) {
            return "redirect:/login";
        }

        Task task = taskService.submit(currentMember, submissionUrl);

        SubmissionInfo info = new SubmissionInfo();
        info.setCurrentLevel(currentMember.getCurrentLevel());
        info.setScoreStatus(task.getScoreStatus());

        model.addAttribute("info", info);
        return "task/submissionDone";
    }
}
