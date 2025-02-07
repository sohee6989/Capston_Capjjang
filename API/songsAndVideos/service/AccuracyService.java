package com.example._4.service;

import com.example._4.dto.AccuracyRequestDto;
import com.example._4.dto.AccuracyResponseDto;
import com.example._4.entity.AccuracyResult;
import com.example._4.repository.AccuracyResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccuracyService {
    private final AccuracyResultRepository accuracyResultRepository;

    // 정확도 평가 수행 (Mediapipe 기반 점수 계산)
    public AccuracyResponseDto evaluateAccuracy(AccuracyRequestDto requestDto) {
        List<double[]> expertKeypoints = getExpertKeypoints(requestDto.getSongId());
        List<double[]> userKeypoints = requestDto.getUserKeypoints();

        double accuracyScore = calculatePoseSimilarity(expertKeypoints, userKeypoints);
        String feedback = getFeedbackFromScore(accuracyScore);

        return AccuracyResponseDto.builder()
                .songId(requestDto.getSongId())
                .accuracyScore(accuracyScore)
                .feedback(feedback)
                .evaluatedAt(LocalDateTime.now())
                .build();
    }


    // 정확도 평가 결과 조회 (최신 평가 결과 반환)
    public AccuracyResult getAccuracyResult(Long songId) {
        return accuracyResultRepository.findFirstBySongIdOrderByEvaluatedAtDesc(songId)
                .orElseThrow(() -> new RuntimeException("No accuracy result found for songId: " + songId));
    }

    // 피드백 반환
    public String getFeedback(Long songId) {
        AccuracyResult result = getAccuracyResult(songId);
        return result.getFeedback();
    }

    // 정확도 평가 결과 저장
    public void saveResult(AccuracyResult result) {
        result.setEvaluatedAt(LocalDateTime.now());
        accuracyResultRepository.save(result);
    }

    // 사용자의 모든 평가 결과 조회
    public List<AccuracyResponseDto> getAllResults(Long userId) {
        List<AccuracyResult> results = accuracyResultRepository.findByUserId(userId);
        return results.stream().map(result -> AccuracyResponseDto.builder()
                        .songId(result.getSongId())
                        .accuracyScore(result.getAccuracyScore())
                        .feedback(result.getFeedback())
                        .evaluatedAt(result.getEvaluatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    // DTW + Euclidean Distance 기반 포즈 유사도 계산
    private double calculatePoseSimilarity(List<double[]> expertKeypoints, List<double[]> userKeypoints) {
        double totalSimilarity = 0;
        int frameCount = Math.min(expertKeypoints.size(), userKeypoints.size());

        for (int i = 0; i < frameCount; i++) {
            totalSimilarity += compareSingleFrame(expertKeypoints.get(i), userKeypoints.get(i));
        }

        return (totalSimilarity / frameCount) * 100; // 100점 만점 기준 변환
    }

    // 개별 프레임 비교 (Euclidean Distance)
    private double compareSingleFrame(double[] expertFrame, double[] userFrame) {
        double distance = 0;
        for (int i = 0; i < expertFrame.length; i++) {
            distance += Math.pow(expertFrame[i] - userFrame[i], 2);
        }
        return Math.sqrt(distance); // 유클리드 거리 반환
    }

    // 점수 기반 피드백 생성
    private String getFeedbackFromScore(double score) {
        if (score >= 95) return "Perfect! Great job!";
        if (score >= 85) return "Good! You're almost there!";
        if (score >= 75) return "Normal! Keep going!";
        if (score >= 60) return "Nice try! You're getting there!";
        if (score > 15) return "Good effort! Keep it up!";
        return "It's Okay... Don't give up!";
    }

    // 전문가 키포인트 데이터 불러오기 (DB 또는 JSON 파일에서 가져오기?)
    private List<double[]> getExpertKeypoints(Long songId) {
        // TODO: 전문가 데이터 저장소에서 가져오는 로직 구현
        return List.of();
    }
}
