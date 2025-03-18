package capston.capston_spring.controller;

import capston.capston_spring.dto.SongDto;
import capston.capston_spring.entity.Song;
import capston.capston_spring.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/song")
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;

    /** 모든 노래 리스트 조회 **/
    @GetMapping("/all")
    public ResponseEntity<List<Song>> getAllSongs() {
        return ResponseEntity.ok(songService.getAllSongs());
    }

    /** 검색 (제목 또는 가수 또는 둘 다 검색 가능) **/
    @GetMapping("/search")
    public ResponseEntity<List<Song>> searchSongs(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String artist) {

        // 제목과 가수 중 하나라도 포함된 노래 찾기
        List<Song> songs = songService.searchSongs(title, artist);

        if (songs.isEmpty()) {
            return ResponseEntity.status(404).body(null); // 검색 결과 없을 경우
        }

        return ResponseEntity.ok(songs);
    }

    /** 곡의 댄스 가이드 영상 및 연습 구간 정보 조회 **/
    @GetMapping("/{songId}/practice-info")
    public ResponseEntity<Map<String, Object>> getPracticeInfo(@PathVariable Long songId, @RequestParam(name = "mode", required = false) String mode) {
        String danceGuidePath = songService.getDanceGuidePath(songId);
        Optional<Object[]> practiceSections = songService.getPracticeSections(songId);

        Map<String, Object> response = new HashMap<>();
        response.put("danceGuidePath", danceGuidePath);

        if (practiceSections.isPresent()) {
            Object[] sections = practiceSections.get();
            if (mode != null) {
                switch (mode) {
                    case "verse":
                        response.put("startTime", sections[0]); // 1절 시작 시간
                        response.put("endTime", sections[1]);   // 1절 끝나는 시간
                        break;
                    case "highlight":
                        response.put("startTime", sections[2]); // 하이라이트 시작 시간
                        response.put("endTime", sections[3]);   // 하이라이트 끝나는 시간
                        break;
                    default:
                        response.put("message", "Invalid mode. Use 'verse' or 'highlight'.");
                }
            }
        }
        return ResponseEntity.ok(response);
    }

    /** 노래 저장 (DTO 기반)
     * TODO: ADMIN 권한으로 변경하기
     * **/
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Song> saveSong(@RequestBody SongDto songDto) {
        return ResponseEntity.ok(songService.saveSong(songDto));
    }
}
