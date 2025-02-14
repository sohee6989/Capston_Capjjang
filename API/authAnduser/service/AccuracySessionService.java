package capston.capston_spring.service;

import capston.capston_spring.dto.AccuracySessionResponse;
import capston.capston_spring.entity.AccuracySession;
import capston.capston_spring.repository.AccuracySessionRepository;
import capston.capston_spring.repository.SongRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AccuracySessionService {
    private final AccuracySessionRepository accuracySessionRepository;
    private final SongRepository songRepository;

    /** 사용자 연습 기록 조회 (my score) **/
    public List<AccuracySessionResponse> getAccuracyHistory(Long userId) {
        List<AccuracySession> sessions = this.accuracySessionRepository.findByUserId(userId);

        return sessions.stream().map(session -> new AccuracySessionResponse(
                session.getSong().getTitle(),
                session.getSong().getCoverImagePath(),
                session.getSong().getArtist(),
                session.getScore()
        )).collect(Collectors.toList());
    }
}
