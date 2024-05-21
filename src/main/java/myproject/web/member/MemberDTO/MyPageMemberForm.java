package myproject.web.member.MemberDTO;

import lombok.Data;
import myproject.domain.member.Grade;
import myproject.domain.member.Member;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class MyPageMemberForm {

    private Long id;

    private String username;
    private String password;
    private String email;
    private String phone;

    private String auto;
    private String auId;

    private Grade grade;

    private String zipcode;
    private String address1;
    private String address2;

    private String photo_name;
    private Date reg_date;
    private Date modify_date;

    private String now_password;
    private MultipartFile upload;

    public MyPageMemberForm(Member member) {

        this.id = member.getId();
        this.username = member.getUsername();
        this.password = member.getPassword();
        this.email = member.getEmail();
        this.phone = member.getPhone();

        this.grade = member.getGrade();
        this.zipcode = member.getZipcode();
        this.address1 = member.getAddress1();
        this.address2 = member.getAddress2();
        this.photo_name = member.getPhoto_name();
        this.reg_date = member.getReg_date();
        this.modify_date = member.getModify_date();

    }
}
