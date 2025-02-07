package com.example._4.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "accuracy_results")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccuracyResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;  // 사용자 ID

    @Column(nullable = false)
    private Long songId;  // 평가된 노래 ID

    @Column(nullable = false)
    private double accuracyScore;  // 정확도 점수

    @Column(nullable = false)
    private String feedback;  // 피드백 내용

    @Column(nullable = false)
    private LocalDateTime evaluatedAt;  // 평가 시간 ??
}
