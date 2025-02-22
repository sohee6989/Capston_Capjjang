package capston.capston_spring.controller;

import capston.capston_spring.entity.RecordedVideo;
import capston.capston_spring.service.VideoService;
import capston.capston_spring.dto.RecordedVideoDto;
import capston.capston_spring.dto.MyVideoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recorded-video")
public class RecordedVideoController {

    private final VideoService videoService;

    public RecordedVideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    // 사용자의 전체 녹화 영상 조회 (MyVideoResponse DTO 반환)
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MyVideoResponse>> getRecordedVideosByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(videoService.getAllVideosByUser(userId));
    }

    // 연습 모드 녹화 영상 조회
    @GetMapping("/practice/{sessionId}")
    public ResponseEntity<List<RecordedVideo>> getRecordedVideosByPracticeSession(@PathVariable Long sessionId) {
        return ResponseEntity.ok(videoService.getRecordedVideosByPracticeSession(sessionId));
    }

    // 특정 챌린지 모드 녹화 영상 조회
    @GetMapping("/challenge/{sessionId}")
    public ResponseEntity<List<RecordedVideo>> getRecordedVideosByChallengeSession(@PathVariable Long sessionId) {
        return ResponseEntity.ok(videoService.getRecordedVideosByChallengeSession(sessionId));
    }

    // 녹화된 영상 저장 (DTO 기반)
    @PostMapping
    public ResponseEntity<RecordedVideo> saveRecordedVideo(@RequestBody RecordedVideoDto recordedVideoDto) {
        return ResponseEntity.ok(videoService.saveRecordedVideo(recordedVideoDto));
    }
}

