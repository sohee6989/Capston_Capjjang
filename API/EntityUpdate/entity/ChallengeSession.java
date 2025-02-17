package capston.capston_spring.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString   // 디버깅관련
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ChallengeSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "song_id", nullable = false)
    private Song song;

    // 시작/종료 시간 필요
    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = true)
    private String backgroundUrl;   // 챌린지 배경 URL

}
