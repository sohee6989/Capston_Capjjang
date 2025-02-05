package com.example._4.service;

import com.example._4.entity.Song;
import com.example._4.repository.SongRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SongService {
    private final SongRepository songRepository;

    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    // 특정 ID로 노래 조회
    public Optional<Song> getSongById(Long song_id) {
        return songRepository.findById(song_id);
    }

    // 1-1. 모두 선택 후 노래 리스트 조회
    public List<Song> getSongsByMode(String mode) {
        return songRepository.findAll();
    }

    // 1-2. 제목 또는 아티스트명 검색
    public List<Song> searchSongs(String query) {
        List<Song> byTitle = songRepository.findByTitleContainingIgnoreCase(query);
        List<Song> byArtist = songRepository.findByArtistContainingIgnoreCase(query);

        // 중복 제거 후 리스트 반환
        Set<Song> resultSet = new HashSet<>();

        if (byTitle != null) {
            resultSet.addAll(byTitle);
        }
        if (byArtist != null) {
            resultSet.addAll(byArtist);
        }

        return new ArrayList<>(resultSet);
    }
}
