package capston.capston_spring.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserProfile {

    private String name;
    private String email;
    private String password;
    private String profileImageUrl;
}
