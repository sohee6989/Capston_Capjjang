// 사용자가 평가 결과를 저장할 때 필요한 데이터만 받기 가능
package com.example._4.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AccuracyRequestDto {
    private Long userId;
    private Long songId;
    private List<double[]> userKeypoints;  // 사용자 포즈 데이터 (Mediapipe)
}
