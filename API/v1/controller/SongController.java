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

    /** 노래 제목으로 노래 조회하기 **/
    @GetMapping("/title/{title}")
    public ResponseEntity<List<Song>> getSongsByTitle(@PathVariable String title) {
        return ResponseEntity.ok(songService.getSongsByTitle(title));
    }

    /** 가수로 노래 조회하기 **/
    @GetMapping("/artist/{artist}")
    public ResponseEntity<List<Song>> getSongsByArtist(@PathVariable String artist) {
        return ResponseEntity.ok(songService.getSongsByArtist(artist));
    }

    /** 노래 저장 (DTO 기반) 
     * TODO: ADMIN 권한으로 변경하기
     * **/
    @PostMapping
    public ResponseEntity<Song> saveSong(@RequestBody SongDto songDto) {
        return ResponseEntity.ok(songService.saveSong(songDto));
    }
}
