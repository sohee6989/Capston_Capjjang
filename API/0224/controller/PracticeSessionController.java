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

    /** 연습 모드에서 선택 가능한 곡 리스트 조회 getPracticeModeSongs (수정 : SongController의 getAllSongs을 대신 사용) **/

    /** 사용자의 연습 세션 조회 (수정 : 메소드 이름 간결하게) **/
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PracticeSession>> getByUserId(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(practiceSessionService.getByUserId(userId));
    }

    /** 특정 곡 관련 연습 세션 조회 (수정 : 사용자 ID 추가 및 메서드 이름 간결하게) **/
    @GetMapping("/song/{songId}/user/{userId}")
    public ResponseEntity<List<PracticeSession>> getBySongAndUser(@PathVariable Long songId, @PathVariable Long userId) {
        return ResponseEntity.ok(practiceSessionService.getBySongAndUser(songId, userId));
    }

    /** 곡 선택 후 연습세션 시작 (DTO 기반) **/
    @PostMapping
    public ResponseEntity<PracticeSession> startPracticeSession(@RequestBody PracticeSessionDto sessionDto) {
        return ResponseEntity.ok(practiceSessionService.savePracticeSession(sessionDto));
    }
}
