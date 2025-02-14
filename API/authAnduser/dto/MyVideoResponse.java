package capston.capston_spring.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MyVideoResponse {
    private Long videoId;
    private String songTitle;
    private String artist;
    private String mode;
    private String songImgPath;
    private String videoPath;
}
