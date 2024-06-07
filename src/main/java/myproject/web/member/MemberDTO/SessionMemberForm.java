package myproject.web.member.MemberDTO;

import lombok.Data;
import myproject.domain.member.Grade;
import myproject.domain.member.Member;
import myproject.web.file.UploadFile;

@Data
public class SessionMemberForm {

    private Long id;
    private String username;
    private String email;
    private UploadFile profileImage;
    private Grade grade;

    public SessionMemberForm(Long id, String username, String email, UploadFile profileImage, Grade grade) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.profileImage = profileImage;
        this.grade = grade;
    }

    public void updateProfileImage(UploadFile profileImage) {
        this.profileImage = profileImage;
    }

    public SessionMemberForm(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.email = member.getEmail();
        this.profileImage = member.getProfileImage();
        this.grade = member.getGrade();
    }
}
