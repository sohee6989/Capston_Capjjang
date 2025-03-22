package capston.capston_spring.dto;

import capston.capston_spring.entity.VideoMode;
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
    private VideoMode mode;
    private String songImgPath;
    private String videoPath;
}
