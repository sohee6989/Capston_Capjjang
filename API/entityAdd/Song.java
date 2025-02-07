package capston.capston_spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** 곡 정보 관리 **/
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String artist;
    private int bpm;
    private int duration;

    private String audioFilePath;
    private String danceGuidePath;
    private String avatarVideoWithAudioPath;
    private String coverImagePath;

}
