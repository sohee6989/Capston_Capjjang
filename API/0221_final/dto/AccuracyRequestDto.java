package capston.capston_spring.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccuracyRequestDto {
    private Long userId;
    private Long songId;
    private Long accuracySessionId; // 정확도 세션과 연결할 ID 추가
    private String videoPath;
}
