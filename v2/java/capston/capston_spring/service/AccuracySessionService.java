// mediapipe 사용의 경우 파이썬의 결과를 가져와서 데이터 저장 및 반환만 담단
// 임의로 써둠 나중에 수정할 것

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
import org.springframework.beans.factory.annotation.Value; // [수정] application.properties 값 가져오기 위해 추가
import org.springframework.core.io.FileSystemResource; // [추가] FileSystemResource import
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AccuracySessionService {
    private final AccuracySessionRepository accuracySessionRepository;
    private final SongRepository songRepository;
    private final UserRepository userRepository;
    @Value("${flask.api.analyze}") // [수정] application.properties에서 Flask API URL 가져오기
    private String flaskAnalyzeUrl;

    @Value("${flask.api.segment}")
    private String flaskSegmentUrl; // [추가] Flask의 `/segment-video` 엔드포인트 URL

    @Value("${flask.api.process}")
    private String flaskProcessUrl; // [추가] Flask의 `/process-video` 엔드포인트 URL


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

    /** Flask 서버와 통신하여 비디오 정확도 분석 후 저장 (수정 : 유저 및 곡 정보 조회 간략화) **/
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

    /** 안무 실루엣 생성 (Flask `/segment-video` 호출) **/
    public String segmentVideo(MultipartFile videoFile) throws IOException {
        File tempFile = convertMultiPartToFile(videoFile); // `MultipartFile` → `File` 변환

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<FileSystemResource> request = new HttpEntity<>(new FileSystemResource(tempFile), headers);
        ResponseEntity<Map> response = restTemplate.exchange(flaskSegmentUrl, HttpMethod.POST, request, Map.class);

        tempFile.delete(); // 사용 후 임시 파일 삭제

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Failed to generate silhouette video.");
        }

        return (String) response.getBody().get("output_video");
    }

    /** 네온 외곽선 적용 (Flask `/process-video` 호출) **/
    public String processVideo(MultipartFile videoFile) throws IOException {
        File tempFile = convertMultiPartToFile(videoFile); // `MultipartFile` → `File` 변환

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<FileSystemResource> request = new HttpEntity<>(new FileSystemResource(tempFile), headers);
        ResponseEntity<Map> response = restTemplate.exchange(flaskProcessUrl, HttpMethod.POST, request, Map.class);

        tempFile.delete(); // 사용 후 임시 파일 삭제

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Failed to apply neon effect.");
        }

        return (String) response.getBody().get("output_video");
    }

    /** MultipartFile을 File로 변환하는 헬퍼 메서드 **/
    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }
        return convFile;
    }
}
    /** 사용자 연습 기록 조회 getUserAccuracyHistory (수정 : 메서드 삭제) **/
