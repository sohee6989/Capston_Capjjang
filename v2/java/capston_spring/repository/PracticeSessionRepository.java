package capston.capston_spring.repository;

import capston.capston_spring.entity.PracticeSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PracticeSessionRepository extends JpaRepository<PracticeSession, Long> {

    // 사용자 ID로 연습 세션 조회 (단일 사용자)
    List<PracticeSession> findByUserId(Long userId);

    // 곡 ID와 사용자 ID로 연습 세션 조회
    List<PracticeSession> findBySongIdAndUserId(Long songId, Long userId);

    // 사용자의 연습 세션 모두 삭제
    void deleteByUserId(Long id);
}
