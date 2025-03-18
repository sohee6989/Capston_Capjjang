package capston.capston_spring.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccuracySessionResponse {
    private String songTitle;
    private String songImgPath;
    private String artist;
    private Integer score;
    private String feedback;
    private String accuracyDetails;         // 동작별 정확도 데이터 포함
}
