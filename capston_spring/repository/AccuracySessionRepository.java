package capston.capston_spring.repository;

import capston.capston_spring.entity.AccuracySession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccuracySessionRepository extends JpaRepository<AccuracySession, Long> {

    List<AccuracySession> findByUserId(Long userId);
    List<AccuracySession> findByUserIdAndSongId(Long userId, Long songId);
    void deleteByUserId(Long id);


    /** 특정 세션 ID로 정확도 세션 조회 (새로운 메소드 추가) **/
    Optional<AccuracySession> findById(Long sessionId);
}
