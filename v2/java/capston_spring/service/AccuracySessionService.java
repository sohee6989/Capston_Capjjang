// mediapipe 사용의 경우 파이썬의 결과를 가져와서 데이터 저장 및 반환만 담단

package capston.capston_spring.service;

import capston.capston_spring.entity.AccuracySession;
import capston.capston_spring.entity.AppUser;
import capston.capston_spring.entity.Song;
import capston.capston_spring.exception.SongNotFoundException;
import capston.capston_spring.exception.UserNotFoundException;
import capston.capston_spring.repository.AccuracySessionRepository;
import capston.capston_spring.repository.SongRepository;
import capston.capston_spring.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value; // application.properties 값 가져오기 위해 추가
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;


@Service
@AllArgsConstructor
public class AccuracySessionService {
    private final AccuracySessionRepository accuracySessionRepository;
    private final SongRepository songRepository;
    private final UserRepository userRepository;

    @Value("${flask.api.analyze}") // application.properties에서 Flask API URL 가져오기
    private String flaskAnalyzeUrl;


    /** ID 기반 사용자 조회 (수정 : 메소드 추가) **/
    private AppUser getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
    }

    /** ID 기반 곡 조회 (수정 : 메소드 추가) **/
    private Song getSongById(Long songId) {
        return songRepository.findById(songId)
                .orElseThrow(() -> new SongNotFoundException("Song not found: " + songId));
    }

    /** 특정 사용자의 정확도 세션 조회 (수정 : 메소드 이름 간결화) **/
    public List<AccuracySession> getByUserId(Long userId){
        return accuracySessionRepository.findByUserId(userId);
    }

    /** 특정 사용자 & 특정 곡 정확도 세션 조회 (수정 : 메소드 추가)**/
    public List<AccuracySession> getByUserAndSong(Long userId, Long songId) {
        return accuracySessionRepository.findByUserIdAndSongId(userId, songId);
    }

    /** 특정 세션 ID로 정확도 세션 조회 (새로운 메소드 추가) **/
    public Optional<AccuracySession> getSessionById(Long sessionId) {
        return accuracySessionRepository.findById(sessionId);
    }

    /** 정확도 세션 저장 **/
    public AccuracySession saveAccuracySession(AccuracySession session){
        return accuracySessionRepository.save(session);
    }

    /** 정확도 분석 후 결과 저장 (Flask 연동) **/
    public AccuracySession analyzeAndSaveSession(Long userId, Long songId, String videoPath){
        // 유저 및 곡 정보 조회
        AppUser user = getUserById(userId);
        Song song = getSongById(songId);

        // 파이썬 서버에 HTTP 요청 보내기
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("user_id", userId);
        requestBody.put("song_id", songId);
        requestBody.put("video_path", videoPath);
        requestBody.put("song_title", song.getTitle()); // Flask로 곡 제목도 함께 전달

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.exchange(flaskAnalyzeUrl, HttpMethod.POST, request, Map.class); // ✅ [수정] URL을 application.properties에서 가져오도록 변경

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Failed to analyze video accuracy.");
        }

        int accuracyScore = ((Number) response.getBody().get("accuracy_score")).intValue();
        String feedback = (String) response.getBody().get("feedback");
        String accuracyDetails = (String) response.getBody().get("accuracy_details");

        AccuracySession session = new AccuracySession(
                null, user, song, LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(1), accuracyScore, feedback, accuracyDetails
        );

        return accuracySessionRepository.save(session);
    }

    /** 곡 제목으로 실루엣 + 가이드 영상 경로 반환 **/
    public Map<String, String> getVideoPathsBySongTitle(String songTitle) {
        Song song = songRepository.findByTitleIgnoreCase(songTitle)
                .orElseThrow(() -> new SongNotFoundException("Song not found with title: " + songTitle));

        Map<String, String> paths = new HashMap<>();
        paths.put("guideVideoUrl", "http://43.200.171.252:5000/static/output/" + song.getDanceGuidePath());
        paths.put("silhouetteVideoUrl", "http://43.200.171.252:5000/static/output/" + song.getSilhouetteVideoPath());
        return paths;
    }

}
    /** 사용자 연습 기록 조회 getUserAccuracyHistory (수정 : 메서드 삭제) **/
