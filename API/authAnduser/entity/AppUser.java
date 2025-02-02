package capston.capston_spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String password;

    @Column(unique = true)
    private String email;


    // role을 Enum으로 저장 (DB에서는 String 형태로 저장)
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;


    // 프로필 이미지 URL 추가
    private String profileImageUrl;


}
