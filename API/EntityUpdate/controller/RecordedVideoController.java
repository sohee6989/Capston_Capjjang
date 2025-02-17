package capston.capston_spring.controller;

import capston.capston_spring.entity.RecordedVideo;
import capston.capston_spring.service.RecordedVideoService;
import capston.capston_spring.dto.RecordedVideoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recorded-video")
public class RecordedVideoController {

    private final RecordedVideoService recordedVideoService;

    public RecordedVideoController(RecordedVideoService recordedVideoService) {
        this.recordedVideoService = recordedVideoService;
    }

    //사용자의 녹화 영상 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RecordedVideo>> getRecordedVideosByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(recordedVideoService.getRecordedVideosByUser(userId));
    }

    // 연습 모드 녹화 영상 조회
    @GetMapping("/practice/{sessionId}")
    public ResponseEntity<List<RecordedVideo>> getRecordedVideosByPracticeSession(@PathVariable Long sessionId) {
        return ResponseEntity.ok(recordedVideoService.getRecordedVideosByPracticeSession(sessionId));
    }

    // 특정 챌린지 모드 녹화 영상 조회
    @GetMapping("/challenge/{sessionId}")
    public ResponseEntity<List<RecordedVideo>> getRecordedVideosByChallengeSession(@PathVariable Long sessionId) {
        return ResponseEntity.ok(recordedVideoService.getRecordedVideosByChallengeSession(sessionId));
    }

    //녹화된 영상 저장 (DTO 기반)
    @PostMapping
    public ResponseEntity<RecordedVideo> saveRecordedVideo(@RequestBody RecordedVideoDto recordedVideoDto) {
        return ResponseEntity.ok(recordedVideoService.saveRecordedVideo(recordedVideoDto));
    }
}

