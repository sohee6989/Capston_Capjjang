package capston.capston_spring.controller;

import capston.capston_spring.dto.SongDto;
import capston.capston_spring.dto.SongSearchDto;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/song")
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;

    /** 모든 노래 리스트 조회 **/
    @GetMapping("/all")
    public ResponseEntity<Object> getAllSongs() {
        try {
            // SongService에서 반환된 List<Song>을 Map으로 변환하여 반환
            List<Map<String, Object>> songDtos = songService.getAllSongs().stream()
                    .map(song -> {
                        Map<String, Object> songMap = new HashMap<>();
                        songMap.put("title", song.getTitle()); // 곡 제목
                        songMap.put("artist", song.getArtist()); // 아티스트 이름
                        songMap.put("coverImagePath", song.getCoverImagePath()); // 커버 이미지 경로
                        return songMap;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(songDtos); // 성공적으로 노래 리스트 반환

        } catch (Exception e) {
            // 예기치 못한 서버 오류 발생 시 500 오류와 함께 메시지 반환
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Internal Server Error");
            return ResponseEntity.status(500).body(errorResponse); // 서버 오류 응답
        }
    }

    /** 검색 (제목 또는 가수 또는 둘 다 검색 가능) **/
    @GetMapping("/search")
    public ResponseEntity<Object> searchSongs(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String artist) {

        // 제목과 가수 중 하나라도 포함된 노래 찾기
        List<Map<String, Object>> songs = songService.searchSongs(title, artist).stream()
                .map(song -> {
                    Map<String, Object> songMap = new HashMap<>();
                    songMap.put("title", song.getTitle()); // 곡 제목
                    songMap.put("artist", song.getArtist()); // 아티스트 이름
                    songMap.put("coverImagePath", song.getCoverImagePath()); // 커버 이미지 경로
                    return songMap;
                })
                .collect(Collectors.toList());

        if (songs.isEmpty()) {
            // 404 상태 코드와 함께 JSON 오류 메시지 반환
            Map<String, String> response = new HashMap<>();
            response.put("error", "No search results found");
            return ResponseEntity.status(404).body(response); // Map으로 반환
        }

        return ResponseEntity.ok(songs); // 검색 결과 반환
    }

    /** 곡의 댄스 가이드 영상 및 구간 정보 조회 **/
    @GetMapping("/{songId}/song-info")
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
