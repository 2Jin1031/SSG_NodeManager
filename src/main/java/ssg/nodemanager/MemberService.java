package ssg.nodemanager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssg.nodemanager.domain.Member;

import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // 로그인 검증
    public boolean signIn(Member member) {
        Optional<Member> optionalFindMember = memberRepository.findByLoginId(member.getLoginId());

        if (optionalFindMember.isEmpty()) {
            throw new IllegalStateException("[ERROR] id를 찾을 수 없음");
        }

        Member findMember = optionalFindMember.get();

        if (!Objects.equals(findMember.getPassword(), member.getPassword())) {
            throw new IllegalStateException("[ERROR] 비밀번호가 틀림");
        }

        return true;
    }
}
