package ssg.nodemanager.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssg.nodemanager.domain.Member;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {


//    public Member findByLoginId(String loginId) {
//        return em.createQuery("select m from Member m where m.loginId = :loginId", Member.class)
//                .setParameter("loginId", loginId)
//                .getSingleResult();
//    }


    Optional<Member> findByLoginId(String loginId);

    Optional<Member> findById(Long id);
}
