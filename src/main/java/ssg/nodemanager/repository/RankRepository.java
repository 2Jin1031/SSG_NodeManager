package ssg.nodemanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssg.nodemanager.domain.Rank;

@Repository
public interface RankRepository extends JpaRepository<Rank, Long> {
}
