package com.example._4.entity; 

import jakarta.persistence.*; 
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_video")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId; // 촬영한 사용자 ID

    @Lob
    @Column(columnDefinition = "LONGBLOB") // 촬영된 안무 영상 저장
    private byte[] videoData;

    @Column(nullable = false)
    private LocalDateTime recordedAt; // 촬영된 시간

    @Column(nullable = false)
    private String mode; // 연습,챌린지 모드 구분
}
