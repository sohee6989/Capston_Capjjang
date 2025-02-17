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

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;


    // role을 Enum으로 저장 (DB에서는 String 형태로 저장)
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;


    // 프로필 이미지 URL 추가
    private String profileImageUrl;

    // 생성자 추가 (OAuth 회원가입 시 사용)
    public AppUser(String email, String name, Role role, String profileImageUrl) {
        this.email = email;
        this.name = name;
        this.password = "";  // OAuth 로그인 사용자는 비밀번호 없음
        this.role = role;
        this.profileImageUrl = profileImageUrl;
    }
}
