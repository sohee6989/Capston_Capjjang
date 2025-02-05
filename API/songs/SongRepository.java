package com.example._4.repository;

import com.example._4.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    // ✅ 제목 검색 (대소문자 구분 없음)
    List<Song> findByTitleContainingIgnoreCase(String title);

    // ✅ 아티스트명 검색 (대소문자 구분 없음)
    List<Song> findByArtistContainingIgnoreCase(String artist);
}
