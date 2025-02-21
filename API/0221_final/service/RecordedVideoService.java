package capston.capston_spring.service;

import capston.capston_spring.dto.RecordedVideoDto;
import capston.capston_spring.entity.AppUser;
import capston.capston_spring.entity.ChallengeSession;
import capston.capston_spring.entity.PracticeSession;
import capston.capston_spring.entity.RecordedVideo;
import capston.capston_spring.repository.ChallengeSessionRepository;
import capston.capston_spring.repository.PracticeSessionRepository;
import capston.capston_spring.repository.RecordedVideoRepository;
import capston.capston_spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecordedVideoService {

    private final RecordedVideoRepository recordedVideoRepository;
    private final UserRepository userRepository;
    private final PracticeSessionRepository practiceSessionRepository;
    private final ChallengeSessionRepository challengeSessionRepository;

    public RecordedVideo saveRecordedVideo(RecordedVideoDto dto) {
        RecordedVideo video = convertToEntity(dto);
        return recordedVideoRepository.save(video);
    }

    private RecordedVideo convertToEntity(RecordedVideoDto dto) {
        AppUser user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + dto.getUserId()));

        PracticeSession practiceSession = null;
        if (dto.getPracticeSessionId() != null) {
            practiceSession = practiceSessionRepository.findById(dto.getPracticeSessionId())
                    .orElseThrow(() -> new RuntimeException("PracticeSession not found with ID: " + dto.getPracticeSessionId()));
        }

        ChallengeSession challengeSession = null;
        if (dto.getChallengeSessionId() != null) {
            challengeSession = challengeSessionRepository.findById(dto.getChallengeSessionId())
                    .orElseThrow(() -> new RuntimeException("ChallengeSession not found with ID: " + dto.getChallengeSessionId()));
        }

        RecordedVideo video = new RecordedVideo();
        video.setUser(user);
        video.setPracticeSession(practiceSession);
        video.setChallengeSession(challengeSession);
        video.setVideoPath(dto.getVideoPath());
        video.setRecordedAt(dto.getRecordedAt());
        video.setDuration(dto.getDuration());

        return video;
    }

    // 사용자의 전체 녹화 영상 조회
    public List<RecordedVideo> getRecordedVideosByUser(Long userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        return recordedVideoRepository.findByUser(user);
    }

    // 연습 모드 녹화 영상 조회
    public List<RecordedVideo> getRecordedVideosByPracticeSession(Long sessionId) {
        PracticeSession practiceSession = practiceSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Practice session not found with ID: " + sessionId));
        return recordedVideoRepository.findByPracticeSession(practiceSession);
    }

    // 챌린지 모드 녹화 영상 조회
    public List<RecordedVideo> getRecordedVideosByChallengeSession(Long sessionId) {
        ChallengeSession challengeSession = challengeSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Challenge session not found with ID: " + sessionId));
        return recordedVideoRepository.findByChallengeSession(challengeSession);
    }
}
