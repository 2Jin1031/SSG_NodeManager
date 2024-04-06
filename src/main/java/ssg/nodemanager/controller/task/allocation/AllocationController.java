package ssg.nodemanager.controller.task.allocation;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ssg.nodemanager.domain.Member;
import ssg.nodemanager.domain.Task;
import ssg.nodemanager.service.member.MemberService;
import ssg.nodemanager.service.task.AllocationService;

@Controller
@RequiredArgsConstructor
public class AllocationController {

    private final AllocationService allocationService;
    private final MemberService memberService;
    
    // 과제할당란
    @GetMapping("/task/allocation")
    public String allocationCheck(HttpServletRequest request,
                                  Model model) {
        // 세션에서 로그인된 사용자 id 가져오기
        Long memberId = (Long) request.getSession().getAttribute("loggedInMemberId");

        // 로그인 id가 없으면 로그인 페이지로 리다이렉트
        if (memberId == null) {
            return "redirect:/login";
        }

        Member currentMember = memberService.findById(memberId); // 데이터베이스에서 최신 Member 정보 조회

        AllocationForm form = allocationService.checkAndPrepareAllocation(currentMember);
        model.addAttribute("form", form);

        Task task = currentMember.getTask();

        // 할당 페이지로 return
        return "task/allocationForm";
    }

    //과제할당란 페이지에 들어왔었는지
    @PostMapping("/task/allocation")
    public String allovationVisit() {
        return null;
    }
}
