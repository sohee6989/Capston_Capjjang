package com.example._4.repository;

import com.example._4.entity.RecordedVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<RecordedVideo, Long> {
    List<RecordedVideo> findBySpeed(double speed); // 특정 재생 속도의 비디오 검색
}
