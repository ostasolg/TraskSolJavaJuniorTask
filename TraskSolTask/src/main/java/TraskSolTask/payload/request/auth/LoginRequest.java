package TraskSolTask.payload.request.auth;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class LoginRequest {
    @NotBlank
    @Size(min = 1, max = 30, message = "Username must be between 1 and 30 characters.")
    private String username;

    @NotBlank
    @Size(min = 5, max = 120, message = "Password must be between 5 and 120 characters.")
    private String password;
}