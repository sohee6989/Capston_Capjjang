package com.example._4.controller;

import com.example._4.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; 
import java.util.Optional;

@RestController
@RequestMapping("/challenge")
@RequiredArgsConstructor
public class ChallengeController {
    private final ChallengeService challengeService;

    // 챌린지 모드에서 노래 재생
    @GetMapping("/audio/{songId}")
    public ResponseEntity<byte[]> getChallengeAudio(@PathVariable Long songId) {
        byte[] audioData = challengeService.getChallengeAudio(songId);
        if (audioData == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(audioData);
    }

    // 챌린지 모드에서 사용할 가상 배경 가져오기
    @GetMapping("/background/{backgroundId}")
    public ResponseEntity<String> getChallengeBackground(@PathVariable Long backgroundId) {
        String backgroundUrl = challengeService.getChallengeBackground(backgroundId);
        if (backgroundUrl == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(backgroundUrl);
    }
}
