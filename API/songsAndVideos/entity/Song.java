package com.example._4.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "songs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String artist;

    @Lob
    @Column(columnDefinition = "LONGBLOB") // 바이너리 데이터 (MP3 파일 저장)
    private byte[] audioData;

    @Column(nullable = true)
    private Integer highlightStart;

    @Column(nullable = true)
    private Integer highlightEnd;

    @Column(nullable = true)
    private Integer verseStart;

    @Column(nullable = true)
    private Integer verseEnd;

    @Column(nullable = true)
    private Integer challengeStart; // 챌린지 시작

    @Column(nullable = true)
    private Integer challengeEnd; // 챌린지 종료
}
