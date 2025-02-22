package capston.capston_spring.repository;

import capston.capston_spring.entity.AppUser;
import capston.capston_spring.entity.ChallengeSession;
import capston.capston_spring.entity.PracticeSession;
import capston.capston_spring.entity.RecordedVideo;
import capston.capston_spring.entity.VideoMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

public interface RecordedVideoRepository extends JpaRepository<RecordedVideo, Long> {
    // 사용자의 저장된 비디오 목록 가져오기
    List<RecordedVideo> findByUser(AppUser user);
    List<RecordedVideo> findByUserIdAndMode(Long userId, VideoMode mode);  // String → VideoMode 사용    List<RecordedVideo> findByPracticeSession(PracticeSession practiceSession);
    List<RecordedVideo> findByChallengeSession(ChallengeSession challengeSession);
    List<RecordedVideo> findByPracticeSession(PracticeSession practiceSession);  // 추가

    void deleteByUserId(Long id);
}
