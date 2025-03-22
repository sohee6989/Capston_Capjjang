package capston.capston_spring.controller;

import capston.capston_spring.entity.PracticeSession;
import capston.capston_spring.service.PracticeSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/practice-session")
@RequiredArgsConstructor
public class PracticeSessionController {

    private final PracticeSessionService practiceSessionService;

    /** 사용자의 연습 세션 조회 **/
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PracticeSession>> getByUserId(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(practiceSessionService.getByUserId(userId));
    }

    /// 특정 곡곡 + 사용자 연습 세션 조회
    @GetMapping("/song/{songId}/user/{userId}")
    public ResponseEntity<List<PracticeSession>> getBySongAndUser(@PathVariable Long songId, @PathVariable Long userId) {
        return ResponseEntity.ok(practiceSessionService.getBySongAndUser(songId, userId));
    }

    /** 특정 구간 반복 연습
     * startTime=30, endTime=45를 입력하면 30~45초 구간만 반복 연습 가능
     * **/

    /** 1절 연습 시작 **/
    @PostMapping("/verse")
    public ResponseEntity<String> startVersePractice(@RequestParam Long userId, @RequestParam Long songId) {
        practiceSessionService.startVersePracticeSession(userId, songId); // ✅ 서비스 호출 연결
        return ResponseEntity.ok("Verse practice session saved for user " + userId + ", song " + songId);
    }

    /** 하이라이트 연습 시작 **/
    @PostMapping("/highlight")
    public ResponseEntity<String> startHighlightPractice(@RequestParam Long userId, @RequestParam Long songId) {
        practiceSessionService.startHighlightPracticeSession(userId, songId); // ✅ 서비스 호출 연결
        return ResponseEntity.ok("Highlight practice session saved for user " + userId + ", song " + songId);
    }
}
