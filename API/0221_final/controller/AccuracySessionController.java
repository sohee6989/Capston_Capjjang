package capston.capston_spring.controller;

import lombok.RequiredArgsConstructor;
import capston.capston_spring.entity.AccuracySession;
import capston.capston_spring.service.AccuracySessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accuracy-sessions")
@RequiredArgsConstructor
public class AccuracySessionController {
    private final AccuracySessionService accuracySessionService;

    /**
     * 영상 분석 및 저장 요청
     */
    @PostMapping("/analyze")
    public ResponseEntity<AccuracySession> analyzeAndSaveSession(
            @RequestParam Long userId,
            @RequestParam Long songId,
            @RequestParam String videoPath) {

        AccuracySession session = accuracySessionService.analyzeAndSaveSession(userId, songId, videoPath);
        return ResponseEntity.ok(session);
    }
}

