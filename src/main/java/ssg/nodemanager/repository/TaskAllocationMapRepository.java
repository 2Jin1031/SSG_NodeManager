package ssg.nodemanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ssg.nodemanager.domain.TaskAllocationMap;

public interface TaskAllocationMapRepository extends JpaRepository<TaskAllocationMap, Long> {

    @Query("SELECT t.url FROM TaskAllocationMap t WHERE t.level = :level")
    String findUrlByLevel(@Param("level") Integer level);
}