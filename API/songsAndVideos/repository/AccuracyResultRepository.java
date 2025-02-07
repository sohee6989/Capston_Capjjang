package com.example._4.repository;

import com.example._4.entity.AccuracyResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccuracyResultRepository extends JpaRepository<AccuracyResult, Long> {

    //  특정 노래의 가장 최신 정확도 평가 결과 가져오기
    Optional<AccuracyResult> findFirstBySongIdOrderByEvaluatedAtDesc(Long songId);

    //  사용자의 정확도 평가 기록 가져오기
    List<AccuracyResult> findByUserId(Long userId);
}
