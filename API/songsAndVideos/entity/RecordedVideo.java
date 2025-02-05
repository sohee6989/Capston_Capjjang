package com.example._4.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "recorded_video")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordedVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String videoUrl; // 비디오 파일 URL

    @Column(nullable = false)
    private LocalDateTime recordedAt; // 녹화 날짜

    @Column
    private Integer startTime = 0; // 기본값 0초로 변경

    @Column
    private Integer endTime = null; // 사용자가 설정 안 하면 null

    @Column
    private double speed = 1.0; // 기본 재생 속도 1.0x
}
