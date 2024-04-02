package ssg.nodemanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssg.nodemanager.domain.Member;
import ssg.nodemanager.domain.Task;

@Repository
public interface TaskRepository extends JpaRepository<Member, Long> {

    void save(Task task);
}
