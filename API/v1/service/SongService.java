package capston.capston_spring.service;

import capston.capston_spring.dto.SongDto;
import capston.capston_spring.entity.Song;
import capston.capston_spring.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SongService {

    private final SongRepository songRepository;

    /** 모든 노래 조회 **/
    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    /** ID로 특정 곡 조회 **/
    public Song getSongById(Long songId) {
        return songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found with ID: " + songId));
    }

    /** 노래 제목으로 검색 **/
    public List<Song> getSongsByTitle(String title) {
        return songRepository.findByTitleContainingIgnoreCase(title);
    }

    /** 가수 이름으로 검색 **/
    public List<Song> getSongsByArtist(String artist) {
        return songRepository.findByArtistContainingIgnoreCase(artist);
    }

    /** 검색어가 제목 또는 아티스트에 포함된 노래 조회 (중복 제거) **/
    public List<Song> searchSongs(String query) {
        return songRepository.findByTitleContainingIgnoreCase(query).stream()
                .distinct()
                .collect(Collectors.toList());
    }

    // 노래 저장
    public Song saveSong(SongDto songDto) {
        Song song = new Song();
        song.setTitle(songDto.getTitle());
        song.setArtist(songDto.getArtist());
        song.setCoverImagePath(songDto.getCoverImagePath());
        return songRepository.save(song);  // Entity를 저장한 후 반환
    }





}