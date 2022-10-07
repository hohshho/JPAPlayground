package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Team;

// JPA의 타입은 1. 엔티티, 2. PK
public interface TeamRepository extends JpaRepository<Team, Long> {
}
