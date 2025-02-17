package capston.capston_spring.controller;

import lombok.RequiredArgsConstructor;
import capston.capston_spring.entity.Song;
import capston.capston_spring.service.SongService;
import capston.capston_spring.dto.SongDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/song")
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;

    @GetMapping("/all")
    public ResponseEntity<List<Song>> getAllSongs() {
        return ResponseEntity.ok(songService.getSongsByMode());
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<List<Song>> getSongsByTitle(@PathVariable String title) {
        return ResponseEntity.ok(songService.getSongsByTitle(title));
    }

    @GetMapping("/artist/{artist}")
    public ResponseEntity<List<Song>> getSongsByArtist(@PathVariable String artist) {
        return ResponseEntity.ok(songService.getSongsByArtist(artist));
    }

    // 노래 저장 (DTO 기반)
    @PostMapping
    public ResponseEntity<Song> saveSong(@RequestBody SongDto songDto) {
        return ResponseEntity.ok(songService.saveSong(songDto));
    }
}
