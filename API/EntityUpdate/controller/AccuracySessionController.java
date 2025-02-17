package capston.capston_spring.controller;

import lombok.RequiredArgsConstructor;
import capston.capston_spring.entity.AccuracySession;
import capston.capston_spring.service.AccuracySessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accuracy-session")
@RequiredArgsConstructor
public class AccuracySessionController {

    private final AccuracySessionService accuracySessionService;

    /*
    public AccuracySessionController(AccuracySessionService accuracySessionService) {
        this.accuracySessionService = accuracySessionService;
    }
     */

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AccuracySession>> getAccuracySessionsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(accuracySessionService.getAccuracySessionsByUser(userId));
    }

    // 특정 곡의 정확도 세션 조회
    @GetMapping("/song/{songId}")
    public ResponseEntity<List<AccuracySession>> getAccuracySessionsBySong(@PathVariable Long songId) {
        return ResponseEntity.ok(accuracySessionService.getAccuracySessionsBySong(songId));
    }

    // 정확도 세션 저장
    @PostMapping
    public ResponseEntity<AccuracySession> saveAccuracySession(@RequestBody AccuracySession session) {
        return ResponseEntity.ok(accuracySessionService.saveAccuracySession(session));
    }

    // Mediapipe 기반 점수 평가 실행 후 결과 저장 (Flask 연동)
    @PostMapping("/analyze")
    public ResponseEntity<AccuracySession> analyzeAndSaveSession(
            @RequestParam Long userId,
            @RequestParam Long songId,
            @RequestParam String videoPath
    ) {
        AccuracySession session = accuracySessionService.analyzeAndSaveSession(userId, songId, videoPath);
        return ResponseEntity.ok(session);
    }
}
