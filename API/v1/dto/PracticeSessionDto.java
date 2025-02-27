package capston.capston_spring.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PracticeSessionDto {
    private Long userId;
    private Long songId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}