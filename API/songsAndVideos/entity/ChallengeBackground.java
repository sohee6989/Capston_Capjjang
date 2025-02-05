package com.example._4.entity; 

import jakarta.persistence.*; 
import lombok.*;

@Entity
@Table(name = "challenge_background")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChallengeBackground {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String backgroundUrl; // 가상 배경 이미지 또는 영상 또는 URL?
}
