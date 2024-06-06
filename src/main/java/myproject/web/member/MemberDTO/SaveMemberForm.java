package myproject.web.member.MemberDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import myproject.domain.member.Address;
import myproject.domain.member.EmbeddedDate;
import myproject.domain.member.Member;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
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


    public Member toEntity() {
        return Member.builder()
                .username(username)
                .password(password)
                .phone(phone)
                .email(email)
                .address(new Address(zipcode, address1, address2))
                .date(new EmbeddedDate(new Date(), null))
                .build();
    }
}
