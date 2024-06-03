package ssg.nodemanager.controller.task;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ssg.nodemanager.config.ConfigConstants;
import ssg.nodemanager.controller.task.submission.SubmissionForm;
import ssg.nodemanager.controller.task.submission.SubmissionInfo;
import ssg.nodemanager.service.task.SubmissionService;
import ssg.nodemanager.service.task.XssService;
import ssg.nodemanager.domain.Member;

@Controller
@RequiredArgsConstructor
public class XssController {

    private final XssService xssService;

    @GetMapping("/task/submissionForm")
    public String showSubmissionForm(Model model,
                                     HttpServletRequest request) {

        HttpSession session = request.getSession();
        Member currentMember = (Member) session.getAttribute("currentMember");

        SubmissionForm form = new SubmissionForm();
        form.setCurrentLevel(currentMember.getCurrentLevel());
        model.addAttribute("form", form);

        String currentContent = (String) session.getAttribute("currentContent");
        model.addAttribute("currentContent", currentContent);
        model.addAttribute("submittedContents", xssService.getSubmittedContents());

        return "task/submissionForm";
    }

    @PostMapping("/task/submitContent")
    public String submitContent(@RequestParam("content") String content,
                                Model model,
                                HttpSession session) {

        // HTML 이스케이프 처리하여 저장
//        String escapedContent = StringEscapeUtils.escapeHtml4(content);
        xssService.addContent(content);

        // 세션에 현재 콘텐츠 저장
        session.setAttribute("currentContent", content);
        model.addAttribute("submittedContents", xssService.getSubmittedContents());

        return "redirect:/task/submissionForm";
    }
}