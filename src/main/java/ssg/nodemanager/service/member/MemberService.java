package ssg.nodemanager.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssg.nodemanager.controller.member.LoginForm;
import ssg.nodemanager.repository.MemberRepository;
import ssg.nodemanager.domain.Member;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // 로그인 검증
    public Member signIn(LoginForm form) {
        Optional<Member> optionalFindMember = memberRepository.findByLoginId(form.getLoginId());

        if (optionalFindMember.isEmpty()) {
            throw new IllegalStateException("[ERROR] id를 찾을 수 없음");
        }

        Member findMember = optionalFindMember.get();

        if (!findMember.isCorrectPassword(form.getPassword())) {
            throw new IllegalStateException("[ERROR] 비밀번호가 틀림");
        }
        return findMember;
    }

    // 찾기
    public Member findByLoginId(String loginId) {
        Optional<Member> optionalFindMember = memberRepository.findByLoginId(loginId);

        if (optionalFindMember.isEmpty()) {
            throw new IllegalStateException("[ERROR] id를 찾을 수 없음");
        }

        return optionalFindMember.get();

    }

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다"));
    }

    public int getMaxLevel() {
        List<Member> members = memberRepository.findAll();
        int maxLevel = 0;
        for (Member member : members) {
            if (member.getCurrentLevel() > maxLevel || maxLevel == 0) {
                maxLevel = member.getCurrentLevel();
            }
        }
        return maxLevel;
    }

    public int getMinLevel(){
        List<Member> members = memberRepository.findAll();
        int minLevel = 0;
        for (Member member : members) {
            if (member.getCurrentLevel() < minLevel || minLevel == 0) {
                minLevel = member.getCurrentLevel();
            }
        }
        return minLevel;
    }

    @Transactional
    public void updateLoginId(Long memberId, String newLoginId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));
        if (member.hasChangedLoginId()) {
            throw new IllegalArgumentException("아이디는 한 번만 변경할 수 있습니다.");
        }
        member.setLoginId(newLoginId);
        member.setHasChangedLoginId(true);
        memberRepository.save(member);
        memberRepository.flush();
    }

    @Transactional
    public void updatePassword(Long memberId, String newPassword) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));
        if (member.hasChangedPassword()) {
            throw new IllegalArgumentException("비밀번호는 한 번만 변경할 수 있습니다.");
        }
        member.setPassword(newPassword);
        member.setHasChangedPassword(true);
        memberRepository.save(member);
        memberRepository.flush();
    }
}
