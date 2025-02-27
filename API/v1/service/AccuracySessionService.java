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
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class AccuracySessionService {
    private final AccuracySessionRepository accuracySessionRepository;
    private final SongRepository songRepository;
    private final UserRepository userRepository;
    private static final String PYTHON_API_URL = "http://127.0.0.1:5000/analyze"; // Flask 서버 주소

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

    /** 특정 곡의 정확도 세션 조회 (수정 : 메소드 삭제) **/

    /** 특정 사용자 & 특정 곡 정확도 세션 조회 (수정 : 메소드 추가)**/
    public List<AccuracySession> getByUserAndSong(Long userId, Long songId) {
        return accuracySessionRepository.findByUserIdAndSongId(userId, songId);
    }

    /** 정확도 세션 저장 **/
    public AccuracySession saveAccuracySession(AccuracySession session){
        return accuracySessionRepository.save(session);
    }

    /** Flask 서버와 통신하여 비디오 정확도 분석 후 저장 (수정 : 유저 및 곡 정보 조회 간략화) **/
    public AccuracySession analyzeAndSaveSession(Long userId, Long songId, String videoPath){
        // 1. 유저 및 곡 정보 조회
        AppUser user = getUserById(userId);
        Song song = getSongById(songId);
        
        // 2. 파이썬 서버에 HTTP 요청 보내기
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("user_id", userId);
        requestBody.put("song_id", songId);
        requestBody.put("video_path", videoPath);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.exchange(PYTHON_API_URL, HttpMethod.POST, request, Map.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Failed to analyze video accuracy. HTTP Status: "
                    + response.getStatusCode() + ", Response Body: " + response.getBody());
        }

        Object accuracyScoreObj = response.getBody().get("accuracy_score");
        String feedback = (String) response.getBody().get("feedback");

        int accuracyScore = (accuracyScoreObj instanceof Number) ? ((Number) accuracyScoreObj).intValue() : 0;

        // 3. 새로운 AccuracySession 생성 및 저장
        AccuracySession session = new AccuracySession(
                null,  // ID는 자동 생성됨
                user,
                song,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(1),  // 예시 (1분 후 종료)
                accuracyScore,
                feedback
        );

        return this.accuracySessionRepository.save(session);
    }


    /** 사용자 연습 기록 조회 getUserAccuracyHistory (수정 : 메서드 삭제) **/
}
