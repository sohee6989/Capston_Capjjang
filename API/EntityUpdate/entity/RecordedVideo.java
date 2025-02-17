package capston.capston_spring.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RecordedVideo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;   // 사용자 정보

    // String mode 를 아래처럼 Enum 사용하는 것으로 수정했습니다
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VideoMode mode;  // PRACTICE, CHALLENGE 등 녹화 모드 (Enum으로 변경함)

    @ManyToOne
    @JoinColumn(name = "practice_session_id")
    private PracticeSession practiceSession;   // 연습 세션 (선택적 연결)

    @ManyToOne
    @JoinColumn(name = "challenge_session_id")
    private ChallengeSession challengeSession; // 챌린지 세션 (선택적 연결)

    @Column(nullable = false)
    private String videoPath;         // 저장된 비디오 파일 경로

    @Column(nullable = false)
    private LocalDateTime recordedAt; // 녹화 시간

    @Column(nullable = false)
    private int duration;             // 영상 길이 (초 단위)
}
