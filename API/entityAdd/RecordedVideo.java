package capston.capston_spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RecordedVideo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;   // 사용자 정보 (필수)

    private String mode;

    @ManyToOne
    @JoinColumn(name = "practice_session_id")
    private PracticeSession practiceSession;   // 연습 세션 (선택적 연결)

    @ManyToOne
    @JoinColumn(name = "challenge_session_id")
    private ChallengeSession challengeSession; // 챌린지 세션 (선택적 연결)

    private String videoPath;         // 저장된 비디오 파일 경로
    private LocalDateTime recordedAt; // 녹화 시간
    private int duration;             // 영상 길이 (초 단위)

}
