package myproject.domain.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import myproject.domain.board.entity.Board;
import myproject.domain.matching.emp.EmpInfo;
import myproject.domain.matching.chat.Chat;
import myproject.domain.matching.chat.ChatRoom;
import myproject.web.file.UploadFile;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "empInfo_id")
    @JsonIgnore
    private EmpInfo empInfo;

    //내가 쓴 글
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Board> board = new ArrayList<>();

    //채팅방 관련 연관관계
    @OneToMany(mappedBy = "member")
    private List<Chat> chats;

    @OneToMany(mappedBy = "sender")
    private List<ChatRoom> sentChatRooms;

    @OneToMany(mappedBy = "receiver")
    private List<ChatRoom> receivedChatRooms;

    //회원가입 시 saveMemberForm -> Member Entity로 변환하기 위한 생성자
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

