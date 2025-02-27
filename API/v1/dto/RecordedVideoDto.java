package capston.capston_spring.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RecordedVideoDto {
    private Long userId;
    private Long practiceSessionId;
    private Long challengeSessionId;
    // private Long accuracySessionId; 정확도 모드에서도 녹화 필요하면 추가
    private String videoPath;
    private LocalDateTime recordedAt;
    private int duration;
}