package com.example._4.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "recorded_video")
@Getter
@Setter
public class RecordedVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "LONGBLOB") // 바이너리 데이터 (MP4 파일 저장)
    private byte[] videoData;

    @Column(nullable = false)
    private LocalDateTime recordedAt; // 녹화 시간

    @Column(nullable = false)
    private double speed; // ️ 재생 속도 (0.5x ~ 2.0x)

    @Column(nullable = false)
    private int startTime; // 재생 시작 시간

    @Column(nullable = true)
    private Integer endTime; // 재생 종료 시간

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    public Integer getEndTime() {
        return endTime;
    }
}
