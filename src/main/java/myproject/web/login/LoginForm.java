package myproject.web.login;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginForm {

    @NotBlank
    private String loginEmail;
    @NotBlank
    private String password;
}
