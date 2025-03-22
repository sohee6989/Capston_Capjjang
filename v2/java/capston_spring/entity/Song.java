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
    private int duration = 0;

    @Column
    private String genre;


    /** 연습 모드 관련 필드 **/
    @Column
    private int verseStartTime = 0; // 1절 시작 시간 (초 단위)

    @Column
    private int verseEndTime = 0; // 1절 끝나는 시간 (초 단위)

    @Column
    private int highlightStartTime = 0; // 하이라이트 시작 시간

    @Column
    private int highlightEndTime = 0; // 하이라이트 끝나는 시간

    @Column
    private int challengeStartTime = 0; // 챌린지 구간 시작 시간

    @Column
    private int challengeEndTime = 0; // 챌린지 구간 끝나는 시간

    /** 파일 경로 관련 필드 **/
    @Column
    private String audioFilePath; // 오디오 파일 경로

    @Column
    private String silhouetteVideoPath;     // 안무 실루엣 영상 경로

    @Column
    private String danceGuidePath; // 댄스 가이드 비디오 경로

    @Column
    private String avatarVideoWithAudioPath; // 아바타 비디오 + 오디오 경로

    @Column
    private String coverImagePath; // 곡 커버 이미지 경로

    /** 사용자 정보 (곡 생성자) **/
    @Column(nullable = false)
    private String createdBy; // 곡을 만든 사용자 정보

    /** 1절 연습 구간 반환 **/
    public int getVerseStartTime() {
        return verseStartTime;
    }

    public int getVerseEndTime() {
        return verseEndTime;
    }

    /** 하이라이트 연습 구간 반환 **/
    public int getHighlightStartTime() {
        return highlightStartTime;
    }

    public int getHighlightEndTime() {
        return highlightEndTime;
    }

    /** 챌린지 모드 연습 구간 반환 **/
    public int getChallengeStartTime() {
        return challengeStartTime;
    }

    public int getChallengeEndTime() {
        return challengeEndTime;
    }

    /** 안무 가이드 영상 경로 반환 **/
    public String getDanceGuidePath() {
        return danceGuidePath;
    }
}
