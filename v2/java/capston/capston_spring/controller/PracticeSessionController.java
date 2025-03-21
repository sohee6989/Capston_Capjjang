package capston.capston_spring.controller;

import capston.capston_spring.dto.PracticeSessionDto;
import capston.capston_spring.entity.PracticeSession;
import capston.capston_spring.service.PracticeSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/practice-session")
@RequiredArgsConstructor
public class PracticeSessionController {

    private final PracticeSessionService practiceSessionService;

    /** 연습 모드에서 안무 실루엣 생성 (Flask `/segment-video` 호출) **/
    @PostMapping("/segment-video")
    public ResponseEntity<String> segmentVideo(@RequestParam("video") MultipartFile videoFile) {
        try {
            String outputVideoPath = practiceSessionService.segmentVideo(videoFile); // Flask에 비디오 전송
            return ResponseEntity.ok(outputVideoPath);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error processing video: " + e.getMessage());
        }
    }

    /** 사용자의 연습 세션 조회 **/
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PracticeSession>> getByUserId(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(practiceSessionService.getByUserId(userId));
    }

    /** 특정 곡 관련 연습 세션 조회 **/
    @GetMapping("/song/{songId}/user/{userId}")
    public ResponseEntity<List<PracticeSession>> getBySongAndUser(@PathVariable Long songId, @PathVariable Long userId) {
        return ResponseEntity.ok(practiceSessionService.getBySongAndUser(songId, userId));
    }

    /** 곡 선택 후 연습세션 시작 (DTO 기반) **/
    @PostMapping
    public ResponseEntity<String> startPracticeSession(@RequestParam("video") MultipartFile videoFile) {
        try {
            String outputVideoPath = practiceSessionService.segmentVideo(videoFile); // Flask로 비디오 전송하여 실루엣 생성
            return ResponseEntity.ok(outputVideoPath); // 실루엣 영상 URL 반환
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error processing video: " + e.getMessage());
        }
    }

    /** 특정 구간 반복 연습
     * startTime=30, endTime=45를 입력하면 30~45초 구간만 반복 연습 가능
     * **/

    /** 1절 연습 시작 **/
    @PostMapping("/verse")
    public ResponseEntity<String> startVersePractice(@RequestParam("video") MultipartFile videoFile) {
        try {
            String outputVideoPath = practiceSessionService.segmentVideo(videoFile); // 실루엣 생성
            return ResponseEntity.ok(outputVideoPath);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error processing video: " + e.getMessage());
        }
    }

    /** 하이라이트 연습 시작 **/
    @PostMapping("/highlight")
    public ResponseEntity<String> startHighlightPractice(@RequestParam("video") MultipartFile videoFile) {
        try {
            String outputVideoPath = practiceSessionService.segmentVideo(videoFile); // 실루엣 생성
            return ResponseEntity.ok(outputVideoPath);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error processing video: " + e.getMessage());
        }
    }
}
