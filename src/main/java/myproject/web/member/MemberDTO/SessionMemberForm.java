package myproject.web.member.MemberDTO;

import lombok.Data;
import myproject.domain.member.Member;

@Data
public class SessionMemberForm {

    private Long id;
    private String username;
    private String email;

    public SessionMemberForm(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }
}
