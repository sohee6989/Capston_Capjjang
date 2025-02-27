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

    /** ID 기반 사용자 조회 (수정 : 메소드 추가) **/
    private AppUser getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

    /** ID 기반 노래 조회 (수정 : 메소드 추가) **/
    private Song getSongById(Long songId) {
        return songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found with ID: " + songId));
    }

    /** (수정 : 유저 및 곡 정보 불러오기 코드 간결화) **/
    private ChallengeSession convertToEntity(ChallengeSessionDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("ChallengeSessionDto cannot be null");
        }

        AppUser user = getUserById(dto.getUserId());
        Song song = getSongById(dto.getSongId());

        ChallengeSession session = new ChallengeSession();
        session.setUser(user);
        session.setSong(song);
        session.setStartTime(dto.getStartTime());
        session.setEndTime(dto.getEndTime());

        return session;
    }

    /** 챌린지 세션 저장 (점수 분석 없이 저장) **/
    public ChallengeSession saveChallengeSession(ChallengeSessionDto dto) {
        ChallengeSession session = convertToEntity(dto);
        return challengeSessionRepository.save(session);
    }

    /** 사용자 ID 기반 챌린지 세션 조회 (수정 : 메소드 이름 간결화) **/
    public List<ChallengeSession> getByUserId(Long userId) {
        return challengeSessionRepository.findByUserId(userId);
    }

    /** 특정 사용자 + 특정 곡 챌린지 세션 조회 (수정 : 메소드 추가) */
    public List<ChallengeSession> getByUserAndSong(Long userId, Long songId) {
        return challengeSessionRepository.findByUserIdAndSongId(userId, songId);
    }


    /** 챌린지에서 사용할 가상 배경 정보 반환 (기본값 없음) **/
    public String getChallengeBackground(Long songId) {
        Song song = getSongById(songId);
        String path = song.getCoverImagePath(); // DB에서 배경 이미지 또는 영상 경로 가져오기

        return (path == null || path.isEmpty())
                ? ""  // 일단은 기본 이미지 없이 빈값 반환
                : path; // URL이나 파일 경로면 그대로 반환
    }


    /** 챌린지에서 사용할 노래의 하이라이트 부분 반환 (DB에서 동적 가져오기) **/
    public String getHighlightPart(Long songId) {
        Song song = getSongById(songId);
        if (song.getHighlightStartTime() == 0 || song.getHighlightEndTime() == 0) {
            return "하이라이트 정보 없음"; // 값이 설정되지 않은 경우 예외 처리
        }
        return String.format("%02d:%02d-%02d:%02d",
                song.getHighlightStartTime() / 60, song.getHighlightStartTime() % 60,
                song.getHighlightEndTime() / 60, song.getHighlightEndTime() % 60);
    }
}
