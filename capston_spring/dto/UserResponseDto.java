package capston.capston_spring.dto;

import capston.capston_spring.entity.AccuracySession;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
    private String profileImageUrl;
    private List<MyVideoResponse> practiceVideos;
    private List<MyVideoResponse> challengeVideos;
    private List<AccuracySession> accuracyHistory;
}
