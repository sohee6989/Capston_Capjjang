package capston.capston_spring.entity;

import jakarta.persistence.*;
import lombok.*;


/** 곡 정보 관리 **/
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String artist;

    @Column(nullable = false)
    private int bpm = 0;

    @Column(nullable = false)
    private int duration = 0;

    @Column(nullable = true)
    private int highlightStartTime;

    @Column(nullable = true)
    private int highlightEndTime;

    @Column(nullable = true)
    private int verseStartTime;

    @Column(nullable = true)
    private int verseEndTime;

    @Column(nullable = true)
    private int challengeStartTime;

    @Column(nullable = true)
    private int challengeEndTime;

    @Column(nullable = false)
    private String audioFilePath; // 오디오 파일 경로

    private String danceGuidePath;        // 댄스 가이드 비디오 경로
    private String avatarVideoWithAudioPath; // 아바타 비디오 + 오디오 경로
    private String coverImagePath;        // 곡 커버 이미지 경로

}
