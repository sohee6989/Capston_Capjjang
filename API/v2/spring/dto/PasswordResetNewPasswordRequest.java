package capston.capston_spring.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetNewPasswordRequest {
    @Email(message = "Invalid email format")
    @NotEmpty(message = "Email is required")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "Invalid email domain")
    private String email;


    @Size(min = 8, message = "Password1 should be at least 8 characters")
    @NotEmpty(message = "New password is required")
    private String newPassword1;

    @Size(min = 8, message = "Password2 should be at least 8 characters")
    @NotEmpty(message = "New password is required")
    private String newPassword2;
}
