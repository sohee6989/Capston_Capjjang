package capston.capston_spring.service;

import lombok.RequiredArgsConstructor;
import capston.capston_spring.entity.Song;
import capston.capston_spring.repository.SongRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SongService {

    private final SongRepository songRepository;

    // 노래 ID로 조회
    public Song getSongById(Long songId) {
        return songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found with ID: " + songId));
    }

    // 모든 모드에서 같은 곡 리스트를 반환 (모드 구분 제거)
    public List<Song> getSongsByMode(String mode) {
        return songRepository.findAll();
    }

    // 제목으로 노래 검색
    public List<Song> getSongsByTitle(String title) {
        return songRepository.findByTitleContainingIgnoreCase(title);
    }

    // 아티스트로 노래 검색
    public List<Song> getSongsByArtist(String artist) {
        return songRepository.findByArtistContainingIgnoreCase(artist);
    }

    // 검색어가 제목 또는 아티스트에 포함된 노래 조회 (중복 제거)
    public List<Song> searchSongs(String query) {
        return songRepository.findByTitleContainingIgnoreCase(query).stream()
                .distinct()
                .collect(Collectors.toList());
    }

    // 노래 저장
    public Song saveSong(Song song) {
        return songRepository.save(song);
    }
}
