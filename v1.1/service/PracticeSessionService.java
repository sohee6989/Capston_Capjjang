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
import java.util.List;

@Service
@RequiredArgsConstructor
public class PracticeSessionService {

    private final PracticeSessionRepository practiceSessionRepository;
    private final UserRepository userRepository;
    private final SongRepository songRepository;

    /** 연습 세션 저장 **/
    public PracticeSession savePracticeSession(PracticeSessionDto dto) {
        PracticeSession session = convertToEntity(dto);
        return practiceSessionRepository.save(session);
    }

    /** (수정 : user, song 코드 간결화)  **/
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

    /** 세션 ID로 연습 세션 조회 (수정 : 메소드 추가) **/
    public PracticeSession getById(Long sessionId){
        return practiceSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found: " + sessionId));
    }

    /** 사용자 ID로 연습 세션 조회 (수정 : 메서드 이름 간결하게 수정 및 사용자 검증 코드 제거) **/
    public List<PracticeSession> getByUserId(Long userId) {
        return practiceSessionRepository.findByUserId(userId);
    }

    /** 특정 곡과 사용자 ID로 연습 세션 조회 (수정 : 메서드 추가)**/
    public List<PracticeSession> getBySongAndUser(Long songId, Long userId) {
        return practiceSessionRepository.findBySongIdAndUserId(songId, userId);
    }

    /** 1절 끝나는 시간을 초 단위로 반환 **/
    public int getVerseEndTime(Long songId) {
        Song song = getSongById(songId);
        return song.getVerseEndTime();
    }

    /** 하이라이트 구간을 초 단위로 반환 **/
    public int[] getHighlightRange(Long songId) {
        Song song = getSongById(songId);
        return new int[]{song.getHighlightStartTime(), song.getHighlightEndTime()};
    }
}

