package capston.capston_spring.controller;

import lombok.RequiredArgsConstructor;
import capston.capston_spring.entity.ChallengeSession;
import capston.capston_spring.entity.AppUser;
import capston.capston_spring.entity.Song;
import capston.capston_spring.service.ChallengeSessionService;
import capston.capston_spring.service.UserService;
import capston.capston_spring.service.SongService;
import capston.capston_spring.dto.ChallengeSessionDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/challenge-session")
public class ChallengeSessionController {

    private final ChallengeSessionService challengeSessionService;
    private final UserService userService;
    private final SongService songService;

    public ChallengeSessionController(ChallengeSessionService challengeSessionService) {
        this.challengeSessionService = challengeSessionService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChallengeSession>> getChallengeSessionsByUser(@PathVariable Long userId) {
        AppUser user = userService.getUserById(userId);
        return ResponseEntity.ok(challengeSessionService.getChallengeSessionsByUser(user));
    }

    @GetMapping("/song/{songId}")
    public ResponseEntity<List<ChallengeSession>> getChallengeSessionsBySong(@PathVariable Long songId) {
        Song song = songService.getSongById(songId);
        return ResponseEntity.ok(challengeSessionService.getChallengeSessionsBySong(song));
    }

    // 챌린지 세션 저장 (DTO 기반)
    @PostMapping
    public ResponseEntity<ChallengeSession> saveChallengeSession(@RequestBody ChallengeSessionDto sessionDto) {
        return ResponseEntity.ok(challengeSessionService.saveChallengeSession(sessionDto));
    }

    // 챌린지에서 쓸 배경
    @GetMapping("/background/{songId}")
    public ResponseEntity<String> getChallengeBackground(@PathVariable Long songId) {
        return ResponseEntity.ok(challengeSessionService.getChallengeBackground(songId));
    }

    // 챌린지에서 쓸 노래 하이라이트 부분
    @GetMapping("/highlight/{songId}")
    public ResponseEntity<String> getHighlightPart(@PathVariable Long songId) {
        return ResponseEntity.ok(challengeSessionService.getHighlightPart(songId));
    }
}
