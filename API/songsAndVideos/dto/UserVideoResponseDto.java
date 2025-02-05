// 마이페이지에서 사용자가 저장한 녹화 영상을 조회할 때 필요한 데이터를 클라이언트에게 전달

package com.example._4.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class UserVideoResponseDto {
    private Long id; // 비디오 ID
    private String mode; // 연습, 챌린지 모드 구분
    private LocalDateTime recordedAt; // 녹화된 시간
}
