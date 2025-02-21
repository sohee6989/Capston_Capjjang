package capston.capston_spring.service;

import capston.capston_spring.dto.ChallengeSessionDto;
import capston.capston_spring.entity.AppUser;
import capston.capston_spring.entity.ChallengeSession;
import capston.capston_spring.entity.Song;
import capston.capston_spring.repository.ChallengeSessionRepository;
import capston.capston_spring.repository.SongRepository;
import capston.capston_spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChallengeSessionService {

    private final ChallengeSessionRepository challengeSessionRepository;
    private final UserRepository userRepository;
    private final SongRepository songRepository;

    private ChallengeSession convertToEntity(ChallengeSessionDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("ChallengeSessionDto cannot be null");
        }

        AppUser user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + dto.getUserId()));

        Song song = songRepository.findById(dto.getSongId())
                .orElseThrow(() -> new RuntimeException("Song not found with ID: " + dto.getSongId()));

        ChallengeSession session = new ChallengeSession();
        session.setUser(user);
        session.setSong(song);
        session.setStartTime(dto.getStartTime());
        session.setEndTime(dto.getEndTime());

        return session;
    }

    public List<ChallengeSession> getChallengeSessionsByUser(AppUser user) {
        return challengeSessionRepository.findByUser(user);
    }

    public List<ChallengeSession> getChallengeSessionsBySong(Song song) {
        return challengeSessionRepository.findBySong(song);
    }

    // 챌린지 세션 저장 (점수 분석 없이 저장)
    public ChallengeSession saveChallengeSession(ChallengeSessionDto dto) {
        ChallengeSession session = convertToEntity(dto);
        return challengeSessionRepository.save(session);
    }

    // 챌린지에서 사용할 가상 배경 정보 반환
    public String getChallengeBackground(Long songId) {
        return "/backgrounds/song_" + songId + ".png";
    }

    // 챌린지에서 사용할 노래의 하이라이트 부분 반환
    public String getHighlightPart(Long songId) {
        return "01:30-01:45";
    }
}
