package ssg.nodemanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ssg.nodemanager.domain.Member;
import ssg.nodemanager.domain.Task;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByMember(Member member);
}
