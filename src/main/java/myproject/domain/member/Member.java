package myproject.domain.member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import myproject.domain.matching.EmpInfo;
import myproject.domain.matching.chat.Chat;
import myproject.domain.matching.chat.ChatRoom;
import myproject.web.file.UploadFile;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String username;
    private String password;
    private String phone;
    private String au_id;
    private String email;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Embedded
    private Address address;

    @Embedded
    private UploadFile profileImage;

    @Embedded
    private EmbeddedDate date;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "empInfo_id")
    private EmpInfo empInfo;
    
    //채팅방 관련 연관관계
    @OneToMany(mappedBy = "member")
    private List<Chat> chats;

    @OneToMany(mappedBy = "sender")
    private List<ChatRoom> sentChatRooms;

    @OneToMany(mappedBy = "receiver")
    private List<ChatRoom> receivedChatRooms;

    //회원가입 시 addMemberForm -> Member Entity로 변환하기 위한 생성자
    public Member createMember(String username, String password, String phone, String email,
                               Address address){

        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.address=address;

        this.grade= Grade.STUDENT;
        return this;
    }

    public void setEmpInfo(EmpInfo empInfo) {
        this.empInfo = empInfo;
    }
}
