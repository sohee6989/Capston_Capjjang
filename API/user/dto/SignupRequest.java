package capston.capston_spring.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    @Email(message = "Invalid email format")
    @NotEmpty(message = "Email is required")
    private String email;

    @NotEmpty(message = "Username is required")
    @Size(min = 3, message = "Username should be at least 3 characters")
    private String username;

    @NotEmpty(message = "Password is required")
    @Size(min = 8, message = "Password1 should be at least 8 characters")
    private String password1;

    @NotEmpty(message = "Password confirmation is required")
    @Size(min = 8, message = "Password2 should be at least 8 characters")
    private String password2;
}
