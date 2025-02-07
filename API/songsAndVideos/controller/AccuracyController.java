package com.example._4.controller;

import com.example._4.dto.AccuracyRequestDto;
import com.example._4.dto.AccuracyResponseDto;
import com.example._4.entity.AccuracyResult;
import com.example._4.service.AccuracyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accuracy/songs")
@RequiredArgsConstructor
public class AccuracyController {
    private final AccuracyService accuracyService;

    // 정확도 모드 최종 평가 (점수 반환)
    @GetMapping("/{song_id}/result")
    public ResponseEntity<AccuracyResult> getAccuracyResult(@PathVariable Long song_id) {
        return ResponseEntity.ok(accuracyService.getAccuracyResult(song_id));
    }

    // 정확도 모드 최종 피드백 (사용자에게 평가 피드백 제공)
    @GetMapping("/{song_id}/feedback")
    public ResponseEntity<String> getAccuracyFeedback(@PathVariable Long song_id) {
        return ResponseEntity.ok(accuracyService.getFeedback(song_id));
    }

    // 정확도 모드 결과 저장
    @PostMapping("/user/profile/my_score")
    public ResponseEntity<String> saveAccuracyScore(@RequestBody AccuracyResult result) {
        accuracyService.saveResult(result);
        return ResponseEntity.ok("Score saved successfully!");
    }

    // 정확도 모드 결과 조회
    @GetMapping("/user/profile/my_score")
    public ResponseEntity<List<AccuracyResponseDto>> getUserScores(@RequestParam Long userId) {
        return ResponseEntity.ok(accuracyService.getAllResults(userId));
    }

    // Mediapipe 기반 점수 평가 (POST)
    @PostMapping("/{song_id}/evaluate")
    public ResponseEntity<AccuracyResponseDto> evaluateAccuracy(
            @PathVariable Long song_id,
            @RequestBody AccuracyRequestDto requestDto) {
        requestDto.setSongId(song_id);  // song_id를 DTO에 설정
        return ResponseEntity.ok(accuracyService.evaluateAccuracy(requestDto));
    }

}
