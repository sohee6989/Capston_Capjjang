package capston.capston_spring.service;

import capston.capston_spring.dto.SongDto;
import capston.capston_spring.dto.SongSearchDto;
import capston.capston_spring.entity.Song;
import capston.capston_spring.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    /** 특정 곡의 댄스 가이드 영상 경로 조회 **/
    public String getDanceGuidePath(Long songId) {
        return songRepository.findDanceGuidePathById(songId)
                .orElseThrow(() -> new RuntimeException("Dance guide video not found for song ID: " + songId));
    }

    /** 특정 곡의 1절/하이라이트 연습 구간 조회 **/
    public Optional<Object[]> getPracticeSections(Long songId) {
        return songRepository.findPracticeSectionsById(songId);
    }

    /** 제목 또는 가수로 노래 검색 (검색용 DTO 반환) **/
    public List<SongSearchDto> searchSongs(String title, String artist) {
        List<Song> songs;

        // 제목이나 가수 중 하나라도 포함되는 경우 검색
        if (title != null && artist != null) {
            songs = songRepository.findByTitleContainingIgnoreCaseAndArtistContainingIgnoreCase(title, artist);
        } else if (title != null) {
            songs = songRepository.findByTitleContainingIgnoreCase(title);
        } else if (artist != null) {
            songs = songRepository.findByArtistContainingIgnoreCase(artist);
        } else {
            songs = songRepository.findAll(); // 검색 조건이 없으면 모든 노래 반환
        }

        // Entity → DTO 변환
        return songs.stream().map(song -> {
            SongSearchDto dto = new SongSearchDto();
            dto.setId(song.getId());
            dto.setTitle(song.getTitle());
            dto.setArtist(song.getArtist());
            dto.setCoverImagePath(song.getCoverImagePath());
            return dto; // 변환된 SongSearchDto 반환
        }).collect(Collectors.toList()); // 최종적으로 SongSearchDto 리스트 반환
    }

    // 노래 저장
    public Song saveSong(SongDto songDto) {
        Song song = new Song();
        song.setTitle(songDto.getTitle());
        song.setArtist(songDto.getArtist());
        song.setCoverImagePath(songDto.getCoverImagePath());

        // JWT 토큰에서 사용자 정보 추출
        String username = getAuthenticatedUsername();

        // 사용자 정보 추가 처리 (예: 저장 시 사용자와 연관된 필드 추가)
        song.setCreatedBy(username);  // 예시로 `createdBy` 필드가 있을 경우, 생성자를 저장하는 로직

        return songRepository.save(song);  // Entity를 저장한 후 반환
    }


    // 인증된 사용자 이름 가져오기
    private String getAuthenticatedUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            return ((User) principal).getUsername();
        } else {
            throw new RuntimeException("Authentication required to access this service");
        }
    }

}
