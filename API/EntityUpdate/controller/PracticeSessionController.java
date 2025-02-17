package capston.capston_spring.controller;

import capston.capston_spring.entity.PracticeSession;
import capston.capston_spring.entity.Song;
import capston.capston_spring.service.PracticeSessionService;
import capston.capston_spring.service.SongService;
import capston.capston_spring.dto.PracticeSessionDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/practice-session")
public class PracticeSessionController {

    private final PracticeSessionService practiceSessionService;
    private final SongService songService;

    public PracticeSessionController(PracticeSessionService practiceSessionService, SongService songService) {
        this.practiceSessionService = practiceSessionService;
        this.songService = songService;
    }

    // 연습 모드에서 선택 가능한 곡 리스트 조회
    @GetMapping("/songs")
    public ResponseEntity<List<Song>> getPracticeModeSongs() {
        return ResponseEntity.ok(songService.getSongsByMode("practice"));
    }

    // 사용자의 연습 세션 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PracticeSession>> getPracticeSessionsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(practiceSessionService.getPracticeSessionsByUser(userId));
    }

    // 특정 곡 관련 연습 세션 조회
    @GetMapping("/song/{songId}")
    public ResponseEntity<List<PracticeSession>> getPracticeSessionsBySong(@PathVariable Long songId) {
        return ResponseEntity.ok(practiceSessionService.getPracticeSessionsBySong(songId));
    }

    // 곡 선택 후 연습 세션 시작 (DTO 기반)
    @PostMapping
    public ResponseEntity<PracticeSession> startPracticeSession(@RequestBody PracticeSessionDto sessionDto) {
        return ResponseEntity.ok(practiceSessionService.savePracticeSession(sessionDto));
    }
}
