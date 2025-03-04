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

    public List<Song> searchSongs(String title, String artist) {
        // 제목이나 가수 중 하나라도 포함되는 경우 검색
        if (title != null && artist != null) {
            return songRepository.findByTitleContainingIgnoreCaseAndArtistContainingIgnoreCase(title, artist);
        } else if (title != null) {
            return songRepository.findByTitleContainingIgnoreCase(title);
        } else if (artist != null) {
            return songRepository.findByArtistContainingIgnoreCase(artist);
        } else {
            return songRepository.findAll(); // 검색 조건이 없으면 모든 노래 반환
        }
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
