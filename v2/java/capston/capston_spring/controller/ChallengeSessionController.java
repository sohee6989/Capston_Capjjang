package capston.capston_spring.controller;

import capston.capston_spring.dto.ChallengeSessionDto;
import capston.capston_spring.entity.ChallengeSession;
import capston.capston_spring.service.ChallengeSessionService;
import capston.capston_spring.service.SongService;
import capston.capston_spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/challenge-session")
public class ChallengeSessionController {

    private final ChallengeSessionService challengeSessionService;
    private final UserService userService;
    private final SongService songService;

    /** 사용자 챌린지 세션 조회 **/
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChallengeSession>> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(challengeSessionService.getByUserId(userId));
    }


    /** 특정 사용자 + 특정 곡 챌린지 세션 조회 **/
    @GetMapping("/user/{userId}/song/{songId}")
    public ResponseEntity<List<ChallengeSession>> getByUserAndSong(@PathVariable Long userId, @PathVariable Long songId) {
        return ResponseEntity.ok(challengeSessionService.getByUserAndSong(userId, songId));
    }

    /** 챌린지 세션 저장 (DTO 기반) **/
    @PostMapping
    public ResponseEntity<ChallengeSession> saveChallengeSession(@RequestBody ChallengeSessionDto sessionDto) {
        return ResponseEntity.ok(challengeSessionService.saveChallengeSession(sessionDto));
    }

    /** 챌린지에서 쓸 배경 **/
    @GetMapping("/background/{songId}")
    public ResponseEntity<String> getChallengeBackground(@PathVariable Long songId) {
        return ResponseEntity.ok(challengeSessionService.getChallengeBackground(songId));
    }

    /** 챌린지에서 쓸 노래 하이라이트 부분 **/
    @GetMapping("/highlight/{songId}")
    public ResponseEntity<String> getHighlightPart(@PathVariable Long songId) {
        return ResponseEntity.ok(challengeSessionService.getHighlightPart(songId));
    }
}
