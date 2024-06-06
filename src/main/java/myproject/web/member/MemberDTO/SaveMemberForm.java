package myproject.web.member.MemberDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SaveMemberForm {

    @NotBlank
    private String username;

    @Pattern(regexp = "^[A-Za-z0-9]{4,12}$")
    private String password;

    private String phone;

    @NotBlank
    @Email
    private String email;


    private String zipcode;
    private String address1;
    private String address2;
}
