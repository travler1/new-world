package myproject.domain.member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;
    private String password;
    private String phone;
    private String au_id;
    private String email;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    private String zipcode;
    private String address1;
    private String address2;

    private byte[] photo;
    private String photo_name;
    private Date reg_date;
    private Date modify_date;

    //회원가입 시 addMemberForm -> Member Entity로 변환하기 위한 생성자
    public Member createMember(String username, String password, String phone, String email,
                               String zipcode, String address1, String address2){

        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.zipcode = zipcode;
        this.address1 = address1;
        this.address2 = address2;

        this.grade= Grade.STUDENT;
        return this;
    }
}
