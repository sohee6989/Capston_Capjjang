package capston.capston_spring.repository;

import capston.capston_spring.entity.AppUser;
import capston.capston_spring.entity.ChallengeSession;
import capston.capston_spring.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeSessionRepository extends JpaRepository<ChallengeSession, Long> {
    List<ChallengeSession> findByUser(AppUser user);
    List<ChallengeSession> findBySong(Song song);

    void deleteByUserId(Long id);
}
