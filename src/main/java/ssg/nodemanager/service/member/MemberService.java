package ssg.nodemanager.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssg.nodemanager.controller.member.LoginForm;
import ssg.nodemanager.repository.MemberRepository;
import ssg.nodemanager.domain.Member;

import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // 로그인 검증
    public void signIn(LoginForm form) {
        Optional<Member> optionalFindMember = memberRepository.findByLoginId(form.getLoginId());

        if (optionalFindMember.isEmpty()) {
            throw new IllegalStateException("[ERROR] id를 찾을 수 없음");
        }

        Member findMember = optionalFindMember.get();

        if (!Objects.equals(findMember.getPassword(), form.getPassword())) {
            throw new IllegalStateException("[ERROR] 비밀번호가 틀림");
        }
    }

    // 찾기
    public Member findByLoginId(String loginId) {
        Optional<Member> optionalFindMember = memberRepository.findByLoginId(loginId);

        if (optionalFindMember.isEmpty()) {
            throw new IllegalStateException("[ERROR] id를 찾을 수 없음");
        }

        return optionalFindMember.get();

    }
}
