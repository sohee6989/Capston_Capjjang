package capston.capston_spring.repository;

import capston.capston_spring.entity.AccuracySession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccuracySessionRepository extends JpaRepository<AccuracySession, Long> {

    List<AccuracySession> findByUserId(Long userId);

    void deleteByUserId(Long id);
}
