package ssg.nodemanager.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ssg.nodemanager.domain.Member;
import ssg.nodemanager.domain.ScoreStatus;
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
    public String submissionCheck(HttpServletRequest request, Model model) {
        // 세션에서 로그인된 사용자 정보 가져오기
        Member currentMember = (Member) request.getSession().getAttribute("loggedInMember");

        // 로그인된 사용자가 없으면 로그인 페이지로 리다이렉트
        if (currentMember == null) {
            return "redirect:/login";
        }

        // task의 존재 여부를 확인하는 로직
        boolean taskExists = checkIfTaskExists(currentMember);

        // task가 존재하면 submissionDone 페이지로 리다이렉트
        if (taskExists) {
            getSubmissionInfo(currentMember.getTask(), currentMember, model);
            return "redirect:/task/submissionDone";
        }

        // task가 존재하지 않으면 submissionForm 페이지로 이동
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

        getSubmissionInfo(taskService.submit(currentMember, submissionUrl), currentMember, model);
        return "task/submissionDone";
    }

//    @GetMapping("task/submission/Done")
//    public String submissionDone(@RequestParam("scoreStatus") ScoreStatus scoreStatus) {
//        System.out.println("scoreStatus = " + scoreStatus);
//        return "task/submissionDone";
//    }

//    @PostMapping("task/submissionDone")
//    public String checkScoreStatus(@RequestParam("scoreStatus") ScoreStatus scoreStatus) {
//        if (scoreStatus == ScoreStatus.Success) {
//
//        }
//    }

    private boolean checkIfTaskExists(Member member) {
        Task existingTask = member.getTask();

        return existingTask != null;
    }

    private static void getSubmissionInfo(Task task, Member member, Model model) {
        SubmissionInfo info = new SubmissionInfo();
        info.setCurrentLevel(member.getCurrentLevel());
        info.setScoreStatus(task.getScoreStatus());

        model.addAttribute("info", info);
    }
}
