// 사용자가 평가 결과를 조회할 때 필요한 데이터만 반환 가능

package com.example._4.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AccuracyResponseDto {
    private Long songId;
    private double accuracyScore;
    private String feedback;
    private LocalDateTime evaluatedAt;
}
