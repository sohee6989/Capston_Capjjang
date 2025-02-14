package capston.capston_spring.repository;

import capston.capston_spring.entity.ChallengeSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeSessionRepository extends JpaRepository<ChallengeSession, Long> {
    void deleteByUserId(Long id);
}
