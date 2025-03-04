package capston.capston_spring.controller;

import capston.capston_spring.dto.SongDto;
import capston.capston_spring.entity.Song;
import capston.capston_spring.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /** 노래 저장 (DTO 기반)
     * TODO: ADMIN 권한으로 변경하기
     * **/
    @PostMapping
    public ResponseEntity<Song> saveSong(@RequestBody SongDto songDto) {
        return ResponseEntity.ok(songService.saveSong(songDto));
    }
}
