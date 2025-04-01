// mediapipe 사용의 경우 파이썬의 결과를 가져와서 데이터 저장 및 반환만 담단

package capston.capston_spring.service;

import capston.capston_spring.dto.AccuracySessionDto;
import capston.capston_spring.entity.AccuracySession;
import capston.capston_spring.entity.AppUser;
import capston.capston_spring.entity.Song;
import capston.capston_spring.exception.SongNotFoundException;
import capston.capston_spring.exception.UserNotFoundException;
import capston.capston_spring.repository.AccuracySessionRepository;
import capston.capston_spring.repository.SongRepository;
import capston.capston_spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value; // application.properties 값 가져오기 위해 추가
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;


@Service
@RequiredArgsConstructor
public class AccuracySessionService {
    private final AccuracySessionRepository accuracySessionRepository;
    private final SongRepository songRepository;
    private final UserRepository userRepository;

    @Value("${flask.api.analyze}")
    private String flaskAnalyzeUrl;

    /** ID 기반 곡 조회 (곡 조회는 계속 사용됨) **/
    private Song getSongById(Long songId) {
        return songRepository.findById(songId)
                .orElseThrow(() -> new SongNotFoundException("Song not found: " + songId));
    }

    public List<AccuracySession> getByUserId(Long userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
        return accuracySessionRepository.findByUserId(user.getId());
    }

    /** username 기반 사용자 조회 (기존 + 유지) **/
    private AppUser getUserByUsername(String username) {
        return userRepository.findByName(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
    }

    /** 특정 사용자(username)의 정확도 세션 조회 **/
    public List<AccuracySession> getByUsername(String username) {
        AppUser user = getUserByUsername(username);
        return accuracySessionRepository.findByUserId(user.getId());
    }

    /** 특정 사용자(username) + 곡의 정확도 세션 조회 **/
    public List<AccuracySession> getBySongAndUsername(Long songId, String username) {
        AppUser user = getUserByUsername(username);
        Song song = getSongById(songId);
        return accuracySessionRepository.findByUserIdAndSongId(user.getId(), song.getId());
    }

    /** 특정 세션 ID로 정확도 세션 조회 **/
    public Optional<AccuracySession> getSessionById(Long sessionId) {
        return accuracySessionRepository.findById(sessionId);
    }

    /** AccuracySessionDto 기반 세션 저장 **/
    public AccuracySession saveSessionFromDto(String username, AccuracySessionDto dto) {
        AppUser user = getUserByUsername(username);
        Song song = getSongById(dto.getSongId());

        AccuracySession session = new AccuracySession();
        session.setUser(user);
        session.setSong(song);
        session.setScore(dto.getScore());
        session.setFeedback(dto.getFeedback());
        session.setAccuracyDetails(dto.getAccuracyDetails());
        session.setMode(dto.getMode());
        session.setStartTime(dto.getStartTime());
        session.setEndTime(dto.getEndTime());

        return accuracySessionRepository.save(session);
    }

    /** 정확도 분석 후 결과 저장 (Flask 연동 유지) **/
    public AccuracySession analyzeAndSaveSessionByUsername(String username, Long songId, String videoPath) {
        AppUser user = userRepository.findByName(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username)); // 예외 처리 복원

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new SongNotFoundException("Song not found: " + songId)); // 예외 처리 복원

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("user_id", user.getId());
        requestBody.put("song_id", songId);
        requestBody.put("video_path", videoPath);
        requestBody.put("song_title", song.getTitle());

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.exchange(flaskAnalyzeUrl, HttpMethod.POST, request, Map.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Failed to analyze video accuracy.");
        }

        double accuracyScore = ((Number) response.getBody().get("accuracy_score")).doubleValue();
        String feedback = (String) response.getBody().get("feedback");
        String accuracyDetails = (String) response.getBody().get("accuracy_details");

        AccuracySession session = new AccuracySession();
        session.setUser(user);
        session.setSong(song);
        session.setStartTime(LocalDateTime.now());
        session.setEndTime(LocalDateTime.now().plusMinutes(1));
        session.setScore(accuracyScore);
        session.setFeedback(feedback);
        session.setAccuracyDetails(accuracyDetails);
        session.setMode("full");

        return accuracySessionRepository.save(session);
    }


    /** 곡 제목으로 실루엣 + 가이드 영상 경로 반환 **/
    public Map<String, String> getVideoPathsBySongTitle(String songTitle) {
        Song song = songRepository.findByTitleIgnoreCase(songTitle)
                .orElseThrow(() -> new SongNotFoundException("Song not found with title: " + songTitle));

        Map<String, String> paths = new HashMap<>();
        paths.put("silhouetteVideoUrl", "http://43.200.171.252:5000/static/output/" + song.getSilhouetteVideoPath());
        return paths;
    }

    /** 정확도 세션 시작 - mode (full/highlight) 에 따라 자동 시간 설정 후 저장 **/
    public AccuracySession startAccuracySessionByUsername(String username, Long songId, String mode) {
        AppUser user = getUserByUsername(username);
        Song song = getSongById(songId);

        int startSec, endSec;
        if (mode.equalsIgnoreCase("full")) {
            startSec = song.getFullStartTime();
            endSec = song.getFullEndTime();
        } else if (mode.equalsIgnoreCase("highlight")) {
            startSec = song.getHighlightStartTime();
            endSec = song.getHighlightEndTime();
        } else {
            throw new IllegalArgumentException("Invalid accuracy mode: " + mode);
        }

        AccuracySession session = new AccuracySession();
        session.setUser(user);
        session.setSong(song);
        session.setMode(mode);
        session.setStartTime(LocalDateTime.ofEpochSecond(startSec, 0, java.time.ZoneOffset.UTC));
        session.setEndTime(LocalDateTime.ofEpochSecond(endSec, 0, java.time.ZoneOffset.UTC));
        session.setScore(0.0); // 초기 점수
        session.setFeedback(null); // 초기 피드백 없음
        session.setAccuracyDetails(null); // 초기 상세 없음

        return accuracySessionRepository.save(session);
    }
}
    /** 사용자 연습 기록 조회 getUserAccuracyHistory (수정 : 메서드 삭제) **/
