package capston.capston_spring.repository;

import capston.capston_spring.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findByTitleContainingIgnoreCase(String title);

    List<Song> findByArtistContainingIgnoreCase(String artist);

    List<Song> findByTitleContainingIgnoreCaseAndArtistContainingIgnoreCase(String title, String artist);
//    List<Song> findByMode(String mode);
}
