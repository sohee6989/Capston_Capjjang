package com.example._4.controller;

import com.example._4.entity.Song;
import com.example._4.service.SongService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/{mode}/songs")
public class SongController {
    private final SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    // 1-1. 모드 선택 후 노래 리스트 조회 (GET /{mode}/songs)
    @GetMapping
    public List<Song> getSongsByMode(@PathVariable String mode) {
        return songService.getSongsByMode(mode);
    }

    // 1-2. 노래 검색 (GET /{mode}/songs/search?query={검색어})
    @GetMapping("/search")
    public List<Song> searchSongs(@PathVariable String mode, @RequestParam String query) {
        return songService.searchSongs(query);
    }

    // 2. 원하는 노래 선택 (GET /{mode}/songs/{song_id})
    @GetMapping("/{song_id}")
    public Optional<Song> getSongDetails(@PathVariable String mode, @PathVariable Long song_id) {
        return songService.getSongById(song_id);
    }

    // 3. 1절 연습 (GET /{mode}/songs/{song_id}/verse)
    @GetMapping("/{song_id}/verse")
    public String startVersePractice(@PathVariable String mode, @PathVariable Long song_id) {
        if (!mode.equals("practice") && !mode.equals("accuracy")) {
            return "Practice cannot be started in this mode.";
        }
        return "Starting verse practice: " + songService.getSongById(song_id).get().getTitle();
    }

    // 4. 하이라이트 연습 (GET /{mode}/songs/{song_id}/highlight)
    @GetMapping("/{song_id}/highlight")
    public String startHighlightPractice(@PathVariable String mode, @PathVariable Long song_id) {
        if (!mode.equals("practice") && !mode.equals("accuracy")) {
            return "Practice cannot be started in this mode.";
        }
        return "Starting highlight practice: " + songService.getSongById(song_id).get().getTitle();
    }

    // 0. 챌린지 모드 (동작 없음, UI에만 표시)
    @GetMapping("/{song_id}/challenge")
    public String challengeModeButton(@PathVariable String mode, @PathVariable Long song_id) {
        return "Challenge mode button displayed (No functionality yet)";
    }
}
