package capston.capston_spring.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AccuracySessionDto {
    private Long id;
    private Long userId;
    private Long songId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int score;
    private String videoPath; // 정확도 평가에 사용된 비디오 경로
}
