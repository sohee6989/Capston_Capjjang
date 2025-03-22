package capston.capston_spring.controller;

import capston.capston_spring.dto.AccuracySessionResponse;
import capston.capston_spring.entity.AccuracySession;
import capston.capston_spring.service.AccuracySessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/accuracy-session")
@RequiredArgsConstructor
public class AccuracySessionController {

    private final AccuracySessionService accuracySessionService;

    /** 사용자의 정확도 세션 조회 **/
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AccuracySession>> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(accuracySessionService.getByUserId(userId));
    }

    /** 특정 사용자 + 특정 곡 정확도 세션 조회 **/
    @GetMapping("/user/{userId}/song/{songId}")
    public ResponseEntity<List<AccuracySession>> getByUserAndSong(@PathVariable Long userId, @PathVariable Long songId) {
        return ResponseEntity.ok(accuracySessionService.getByUserAndSong(userId, songId));
    }

    /** 정확도 세션 저장 **/
    @PostMapping
    public ResponseEntity<AccuracySession> saveAccuracySession(@RequestBody AccuracySession session) {
        return ResponseEntity.ok(accuracySessionService.saveAccuracySession(session));
    }

    /** Mediapipe 기반 점수 평가 실행 후 결과 저장 (Flask 연동) **/
    @PostMapping("/analyze")
    public ResponseEntity<AccuracySession> analyzeAndSaveSession(
            @RequestParam Long userId,
            @RequestParam Long songId,
            @RequestParam String videoPath
    ) {
        return ResponseEntity.ok(accuracySessionService.analyzeAndSaveSession(userId, songId, videoPath));
    }

    /** 특정 세션 ID로 정확도 세션 상세 조회 **/
    @GetMapping("/{sessionId}/result")
    public ResponseEntity<AccuracySessionResponse> getSessionResult(@PathVariable Long sessionId) {
        return accuracySessionService.getSessionById(sessionId)
                .map(session -> ResponseEntity.ok(
                        new AccuracySessionResponse(
                                session.getSong().getTitle(),
                                session.getSong().getCoverImagePath(),
                                session.getSong().getArtist(),
                                session.getScore(),
                                session.getFeedback(),
                                session.getAccuracyDetails()
                        )
                ))
                .orElse(ResponseEntity.notFound().build());
    }

    /** 곡 제목으로 실루엣 + 가이드 영상 경로 반환 **/
    @GetMapping("/video-paths")
    public ResponseEntity<Map<String, String>> getVideoPathsBySongTitle(@RequestParam("songName") String songName) {
        return ResponseEntity.ok(accuracySessionService.getVideoPathsBySongTitle(songName));
    }
}
