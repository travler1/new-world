package myproject.web.member.MemberDTO;

import lombok.Builder;
import lombok.Data;
import myproject.domain.member.Address;
import myproject.domain.member.EmbeddedDate;
import myproject.domain.member.Grade;
import myproject.domain.member.Member;
import myproject.web.file.UploadFile;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ReadMemberForm {

    private Long id;

    private String username;
    private String password;
    private String email;
    private String phone;

    private String auto;
    private String auId;

    private Grade grade;
    private Address address;
    private UploadFile profileImage;
    private EmbeddedDate date;

    private String now_password;
    private MultipartFile upload;

    @Builder
    public ReadMemberForm(Member member) {

        this.id = member.getId();
        this.username = member.getUsername();
        this.password = member.getPassword();
        this.email = member.getEmail();
        this.phone = member.getPhone();
        this.grade = member.getGrade();
        this.address = member.getAddress();
        this.profileImage = member.getProfileImage();
        this.date = member.getDate();

    }
}
