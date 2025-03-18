package capston.capston_spring.controller;

import capston.capston_spring.dto.AccuracySessionResponse;
import capston.capston_spring.entity.AccuracySession;
import capston.capston_spring.service.AccuracySessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        AccuracySession session = accuracySessionService.analyzeAndSaveSession(userId, songId, videoPath);
        return ResponseEntity.ok(session);
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
}
