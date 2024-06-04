package ssg.nodemanager.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import ssg.nodemanager.domain.Member;
import ssg.nodemanager.exception.NotLoggedInException;
import ssg.nodemanager.service.member.MemberService;

@Aspect
@Component
@RequiredArgsConstructor
public class LoginCheckAspect {

    private final MemberService memberService;

    @Before("execution(* ssg.nodemanager.controller.task..*(..)) && args(request, ..)")
    public void checkLogin(HttpServletRequest request) throws NotLoggedInException {
        Long memberId = (Long) request.getSession().getAttribute("loggedInMemberId");
        if (memberId == null) {
            throw new NotLoggedInException(); // Custom exception to handle not logged in users
        }

        Member currentMember = memberService.findById(memberId);
        request.getSession().setAttribute("currentMember", currentMember);
    }
}