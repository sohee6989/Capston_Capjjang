package capston.capston_spring.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetVerifyRequest {
    @Email(message = "Invalid email format")
    @NotEmpty(message = "Email is required")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "Invalid email domain")
    private String email;

    @NotEmpty(message = "Verification code is required")
    private String verificationCode;
}
