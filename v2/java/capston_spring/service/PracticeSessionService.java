package capston.capston_spring.service;

import capston.capston_spring.dto.PracticeSessionDto;
import capston.capston_spring.entity.AppUser;
import capston.capston_spring.entity.PracticeSession;
import capston.capston_spring.entity.Song;
import capston.capston_spring.exception.SongNotFoundException;
import capston.capston_spring.exception.UserNotFoundException;
import capston.capston_spring.repository.PracticeSessionRepository;
import capston.capston_spring.repository.SongRepository;
import capston.capston_spring.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PracticeSessionService {

    private final PracticeSessionRepository practiceSessionRepository;
    private final UserRepository userRepository;
    private final SongRepository songRepository;

    /** 1절 연습 세션 시작 (DB의 verse 구간 사용) **/
    public PracticeSession startVersePracticeSession(Long userId, Long songId) {
        AppUser user = getUserById(userId);
        Song song = getSongById(songId);

        PracticeSession session = new PracticeSession();
        session.setUser(user);
        session.setSong(song);

        // DB에서 verse 구간 시간 가져와서 설정
        session.setStartTimeFromSeconds(song.getVerseStartTime());
        session.setEndTimeFromSeconds(song.getVerseEndTime());

        return practiceSessionRepository.save(session);
    }

    /** 하이라이트 연습 세션 시작 (DB의 highlight 구간 사용) **/
    public PracticeSession startHighlightPracticeSession(Long userId, Long songId) {
        AppUser user = getUserById(userId);
        Song song = getSongById(songId);

        PracticeSession session = new PracticeSession();
        session.setUser(user);
        session.setSong(song);

        // DB에서 highlight 구간 시간 가져와서 설정
        session.setStartTimeFromSeconds(song.getHighlightStartTime());
        session.setEndTimeFromSeconds(song.getHighlightEndTime());

        return practiceSessionRepository.save(session);
    }

    // 연습 세션 저장
    public PracticeSession savePracticeSession(PracticeSessionDto dto) {
        PracticeSession session = convertToEntity(dto);
        return practiceSessionRepository.save(session);
    }

    // 프론트에서 사용자 입력으로 시간 받아서 연습 세션 저장할 예정이면 코드 복구
    private PracticeSession convertToEntity(PracticeSessionDto dto) {
        AppUser user = getUserById(dto.getUserId());
        Song song = getSongById(dto.getSongId());

        PracticeSession session = new PracticeSession();
        session.setUser(user);
        session.setSong(song);

        // LocalDateTime -> 초 단위 변환 후 저장
        session.setStartTimeFromSeconds((int) dto.getStartTime().toEpochSecond(ZoneOffset.UTC));
        session.setEndTimeFromSeconds((int) dto.getEndTime().toEpochSecond(ZoneOffset.UTC));

        return session;
    }

    /** 사용자 조회 (수정 : 메소드 추가) **/
    private AppUser getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
    }

    /** ID로 노래 조회 (수정 : 메소드 추가) */
    private Song getSongById(Long songId) {
        return songRepository.findById(songId)
                .orElseThrow(() -> new SongNotFoundException("Song not found: " + songId));
    }

    /** 사용자 ID로 연습 세션 조회 (수정 : 메서드 이름 간결하게 수정 및 사용자 검증 코드 제거) **/
    public List<PracticeSession> getByUserId(Long userId) {
        return practiceSessionRepository.findByUserId(userId);
    }

    /** 특정 곡과 사용자 ID로 연습 세션 조회 (수정 : 메서드 추가)**/
    public List<PracticeSession> getBySongAndUser(Long songId, Long userId) {
        return practiceSessionRepository.findBySongIdAndUserId(songId, userId);
    }

    /** Song 엔티티에서 1절 끝나는 시간 조회 (초 단위) **/
    public int getVerseEndTime(Long songId) {
        Song song = getSongById(songId);
        return song.getVerseEndTime();
    }

    /** Song 엔티티에서 하이라이트 구간 조회 **/
    public int[] getHighlightRange(Long songId) {
        Song song = getSongById(songId);
        return new int[]{song.getHighlightStartTime(), song.getHighlightEndTime()};
    }

    /** 곡 제목으로 실루엣 + 가이드 영상 경로 반환 **/
    public Map<String, String> getPracticeModeVideoPathsBySongTitle(String songTitle) {
        Song song = songRepository.findByTitleIgnoreCase(songTitle)
                .orElseThrow(() -> new SongNotFoundException("Song not found with title: " + songTitle));

        Map<String, String> paths = new HashMap<>();
        paths.put("danceGuideUrl", "http://43.200.171.252:5000/static/output/" + song.getDanceGuidePath());
        paths.put("silhouetteVideoUrl", "http://43.200.171.252:5000/static/output/" + song.getSilhouetteVideoPath());
        return paths;
    }

}

