package capston.capston_spring.service;

import capston.capston_spring.dto.PracticeSessionDto;
import capston.capston_spring.entity.AppUser;
import capston.capston_spring.entity.PracticeSession;
import capston.capston_spring.entity.Song;
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

    /** username 기반으로 1절/하이라이트 연습 세션 시작 (공통 처리) **/
    public PracticeSession startPracticeSessionByUsername(String username, Long songId, String section) {
        AppUser user = getUserByUsername(username); // 예외 통일
        Song song = getSongById(songId);            // 예외 통일

        int startTime;
        int endTime;

        switch (section.toLowerCase()) {
            case "verse":
                startTime = song.getVerseStartTime();
                endTime = song.getVerseEndTime();
                break;
            case "highlight":
                startTime = song.getHighlightStartTime();
                endTime = song.getHighlightEndTime();
                break;
            default:
                throw new IllegalArgumentException("Invalid section: " + section); // 유효성 검증
        }

        PracticeSession session = new PracticeSession();
        session.setUser(user);
        session.setSong(song);
        session.setStartTimeFromSeconds(startTime);
        session.setEndTimeFromSeconds(endTime);
        session.setMode(section);  // mode 설정 추가

        return practiceSessionRepository.save(session);
    }

    /** username 기반 1절 연습 세션 시작 */
    public PracticeSession startVersePracticeSessionByUsername(String username, Long songId) {
        return startPracticeSessionByUsername(username, songId, "verse");
    }

    /** username 기반 하이라이트 연습 세션 시작 **/
    public PracticeSession startHighlightPracticeSessionByUsername(String username, Long songId) {
        return startPracticeSessionByUsername(username, songId, "highlight");
    }

    // 연습 세션 저장 (userId 대신 username 기반으로 수정)
    public PracticeSession savePracticeSession(PracticeSessionDto dto) {
        PracticeSession session = convertToEntity(dto);
        return practiceSessionRepository.save(session);
    }

    private PracticeSession convertToEntity(PracticeSessionDto dto) {
        AppUser user = getUserByUsername(dto.getUsername()); // userId -> username 기반으로 변경
        Song song = getSongById(dto.getSongId());

        PracticeSession session = new PracticeSession();
        session.setUser(user);
        session.setSong(song);

        session.setStartTimeFromSeconds((int) dto.getStartTime().toEpochSecond(ZoneOffset.UTC));
        session.setEndTimeFromSeconds((int) dto.getEndTime().toEpochSecond(ZoneOffset.UTC));
        session.setMode(dto.getMode()); // mode 설정 추가

        return session;
    }

    /** username 기반 사용자 조회 **/
    private AppUser getUserByUsername(String username) {
        return userRepository.findByName(username)  // username → name
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + username)); // 수정됨
    }

    /** ID로 곡 조회 **/
    private Song getSongById(Long songId) {
        return songRepository.findById(songId)
                .orElseThrow(() -> new IllegalArgumentException("곡을 찾을 수 없습니다: " + songId)); // 수정됨
    }

    /** username 기반 사용자 연습 세션 조회 */
    public List<PracticeSession> getByUsername(String username) {
        AppUser user = getUserByUsername(username);
        return practiceSessionRepository.findByUserId(user.getId());
    }

    /** username 기반 곡 + 사용자 연습 세션 조회 */
    public List<PracticeSession> getBySongAndUsername(Long songId, String username) {
        AppUser user = getUserByUsername(username);
        return practiceSessionRepository.findBySongIdAndUserId(songId, user.getId());
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

    /** 실루엣 & 가이드 영상 경로 반환 (곡 제목 기준) **/
    public Map<String, String> getPracticeModeVideoPathsBySongTitle(String songTitle) {
        Song song = songRepository.findByTitleIgnoreCase(songTitle)
                .orElseThrow(() -> new IllegalArgumentException("곡을 찾을 수 없습니다: " + songTitle)); // 수정됨

        Map<String, String> paths = new HashMap<>();
        paths.put("danceGuideUrl", "http://43.200.171.252:5000/static/output/" + song.getDanceGuidePath());
        paths.put("silhouetteVideoUrl", "http://43.200.171.252:5000/static/output/" + song.getSilhouetteVideoPath());
        return paths;
    }
}

