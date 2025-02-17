// mediapipe 사용의 경우 파이썬의 결과를 가져와서 데이터 저장 및 반환만 담단
// 임의로 써둠 나중에 수정할 것

package capston.capston_spring.service;

import lombok.RequiredArgsConstructor;
import capston.capston_spring.entity.AccuracySession;
import capston.capston_spring.entity.AppUser;
import capston.capston_spring.entity.Song;
import capston.capston_spring.repository.AccuracySessionRepository;
import capston.capston_spring.repository.UserRepository;
import capston.capston_spring.repository.SongRepository;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AccuracySessionService {

    private final AccuracySessionRepository accuracySessionRepository;
    private final UserRepository userRepository;
    private final SongRepository songRepository;
    private static final String PYTHON_API_URL = "http://127.0.0.1:5000/analyze"; // Flask 서버 주소

    // 특정 사용자의 정확도 세션 조회
    public List<AccuracySession> getAccuracySessionsByUser(Long userId) {
        return accuracySessionRepository.findByUserId(userId);
    }

    // 특정 곡의 정확도 세션 조회
    public List<AccuracySession> getAccuracySessionsBySong(Long songId) {
        return accuracySessionRepository.findBySongId(songId);
    }

    // 정확도 세션 저장
    public AccuracySession saveAccuracySession(AccuracySession session) {
        return accuracySessionRepository.save(session);
    }

    // Flask 서버와 통신하여 비디오 정확도 분석 후 저장
    public AccuracySession analyzeAndSaveSession(Long userId, Long songId, String videoPath) {
        // 1. 유저 및 곡 정보 조회
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found with ID: " + songId));

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

        return accuracySessionRepository.save(session);
    }
}
