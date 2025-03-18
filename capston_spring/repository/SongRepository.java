package capston.capston_spring.repository;

import capston.capston_spring.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findByTitleContainingIgnoreCase(String title);

    List<Song> findByArtistContainingIgnoreCase(String artist);

    List<Song> findByTitleContainingIgnoreCaseAndArtistContainingIgnoreCase(String title, String artist);
//    List<Song> findByMode(String mode);
    /** 곡의 danceGuidePath(안무 가이드 영상 URL) 조회 **/
    @Query("SELECT s.danceGuidePath FROM Song s WHERE s.id = :id")
    Optional<String> findDanceGuidePathById(@Param("id") Long id);

    /** 특정 곡의 1절/하이라이트 연습 구간 조회 **/
    @Query("SELECT s.verseStartTime, s.verseEndTime, s.highlightStartTime, s.highlightEndTime FROM Song s WHERE s.id = :id")
    Optional<Object[]> findPracticeSectionsById(@Param("id") Long id);
}

