package ssg.nodemanager.controller.task.allocation;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ssg.nodemanager.controller.task.submission.SubmissionForm;
import ssg.nodemanager.domain.Member;
import ssg.nodemanager.domain.Task;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AllocationController {

    // 과제할당란
    @GetMapping("/task/allocation")
    public String allocationCheck(HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {
        // 세션에서 로그인된 사용자 정보 가져오기
        Member currentMember = (Member) request.getSession().getAttribute("loggedInMember");

        // 로그인된 사용자가 없으면 로그인 페이지로 리다이렉트
        if (currentMember == null) {
            return "redirect:/login";
        }

        // 현재 level 확인하는 로직
        Task task = currentMember.getTask();
        if (task.isAllocation()) { // 과제 할당이 가능하다면
            // level 업데이트
            currentMember.setCurrentLevel(currentMember.getNextLevel());
            currentMember.setNextLevel(currentMember.getNextLevel() + 1);

        }
        AllocationForm allocationForm = new AllocationForm();
        Map<Integer, String> allocationMapByLevel = task.getAllocationMapByLevel();
        allocationForm.setCurrectLevel(currentMember.getCurrentLevel());
        allocationForm.setAllocationUrl(allocationMapByLevel.get(currentMember.getCurrentLevel()));

        model.addAttribute("form", allocationForm);
        // 할당 페이지로 return
        return "task/allocationForm";
    }
}
