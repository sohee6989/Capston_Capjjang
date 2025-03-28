package capston.capston_spring.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditPasswordRequest {
    @NotEmpty(message = "현재 비밀번호를 입력하세요.")
    private String currentPassword;

    @NotEmpty(message = "새 비밀번호를 입력하세요.")
    private String newPassword;
}
