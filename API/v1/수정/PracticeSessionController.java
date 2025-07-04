package capston.capston_spring.controller;

import capston.capston_spring.dto.PracticeSessionDto;
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

    /** 특정 곡 관련 연습 세션 조회 **/
    @GetMapping("/song/{songId}/user/{userId}")
    public ResponseEntity<List<PracticeSession>> getBySongAndUser(@PathVariable Long songId, @PathVariable Long userId) {
        return ResponseEntity.ok(practiceSessionService.getBySongAndUser(songId, userId));
    }

    /** 곡 선택 후 연습세션 시작 (DTO 기반) **/
    @PostMapping
    public ResponseEntity<PracticeSession> startPracticeSession(@RequestBody PracticeSessionDto sessionDto) {
        return ResponseEntity.ok(practiceSessionService.savePracticeSession(sessionDto));
    }

    /** 특정 구간 반복 연습
     * startTime=30, endTime=45를 입력하면 30~45초 구간만 반복 연습 가능
     * **/

    /** 1절 연습 시작 **/
    @PostMapping("/verse")
    public ResponseEntity<PracticeSession> startVersePractice(@RequestBody PracticeSessionDto sessionDto) {
        sessionDto.setStartTime(0); // 0초부터 시작
        sessionDto.setEndTime(practiceSessionService.getVerseEndTime(sessionDto.getSongId())); // 1절 끝까지
        return ResponseEntity.ok(practiceSessionService.savePracticeSession(sessionDto));
    }

    /** 하이라이트 연습 시작 **/
    @PostMapping("/highlight")
    public ResponseEntity<PracticeSession> startHighlightPractice(@RequestBody PracticeSessionDto sessionDto) {
        int[] highlightRange = practiceSessionService.getHighlightRange(sessionDto.getSongId());
        sessionDto.setStartTime(highlightRange[0]);
        sessionDto.setEndTime(highlightRange[1]);
        return ResponseEntity.ok(practiceSessionService.savePracticeSession(sessionDto));
    }
}
