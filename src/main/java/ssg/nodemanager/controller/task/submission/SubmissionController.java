package ssg.nodemanager.controller.task.submission;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ssg.nodemanager.domain.Member;
import ssg.nodemanager.domain.ScoreStatus;
import ssg.nodemanager.domain.Task;
import ssg.nodemanager.service.MemberService;
import ssg.nodemanager.service.TaskService;

@Controller
@RequiredArgsConstructor
public class SubmissionController {

    private final TaskService taskService;
    private final MemberService memberService;

    //과제제출란
    @GetMapping("/task/submission")
    public String submissionCheck(HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {
        // 세션에서 로그인된 사용자 정보 가져오기
        Member currentMember = (Member) request.getSession().getAttribute("loggedInMember");

        // 로그인된 사용자가 없으면 로그인 페이지로 리다이렉트
        if (currentMember == null) {
            return "redirect:/login";
        }

        // task의 존재 여부를 확인하는 로직

        Task task = currentMember.getTask();
        if (task == null) { // task가 존재하지 않으면 submissionForm 페이지로 이동
            SubmissionForm form = new SubmissionForm();
            form.setCurrentLevel(currentMember.getCurrentLevel());

            model.addAttribute("form", form);
            return "task/submissionForm";
        }
//        boolean taskExists = checkIfTaskExists(currentMember);

        // task가 존재하면 /task/submission/complete 페이지로 리다이렉트
        getSubmissionInfo(currentMember.getTask(), currentMember, model);
        redirectAttributes.addFlashAttribute("info", model.asMap().get("info"));
        return "redirect:/task/submission/complete";

    }


    @PostMapping("/task/submission")
    public String submit(@RequestParam("url") String submissionUrl, HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {
        // 세션에서 로그인된 사용자 정보 가져오기
        Member currentMember = (Member) request.getSession().getAttribute("loggedInMember");

        // 로그인된 사용자가 없으면 로그인 페이지로 리다이렉트
        if (currentMember == null) {
            return "redirect:/login";
        }

        getSubmissionInfo(taskService.submit(currentMember, submissionUrl), currentMember, model);
        redirectAttributes.addFlashAttribute("info", model.asMap().get("info"));
        return "redirect:/task/submission/complete";
    }

    @GetMapping("task/submission/complete")
    public String submissionDone(@ModelAttribute("info") SubmissionInfo info) {
        System.out.println("info.getCurrentLevel() = " + info.getCurrentLevel());
        System.out.println("info.getScoreStatus() = " + info.getScoreStatus());
        return "task/submissionDone";
    }

    private boolean checkIfTaskExists(Member member) {
        Task existingTask = member.getTask();

        return existingTask != null;
    }

    private static void getSubmissionInfo(Task task, Member member, Model model) {
        SubmissionInfo info = new SubmissionInfo();
        info.setCurrentLevel(member.getCurrentLevel());
        info.setScoreStatus(task.getScoreStatus());
        info.setNextLevel(member.getNextLevel());

        if (ScoreStatus.Success.equals(task.getScoreStatus())) {
            info.setSuccess(true);
        }

        model.addAttribute("info", info);
    }
}
