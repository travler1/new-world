package myproject.domain.matching.chat;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import myproject.domain.member.Member;

import java.lang.module.FindException;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {

    @Id @GeneratedValue
    @Column(name = "chat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "chatRoom_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "senderId")
    private Member member;

    @Column(name = "chat_message", length = 900)
    private String chatMessage;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CHAT_REGDATE")
    private Date chatRegDate;

    @Column(name = "CHAT_READCHECK")
    private int chatReadCheck;

    //==연관관계 편의 메서드==//
    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
        chatRoom.getChatList().add(this);
    }

    public void removeChatRoom(ChatRoom chatRoom) {
        chatRoom.getChatList().remove(this);
        this.chatRoom=null;
    }

    @Builder
    public Chat(ChatRoom chatRoom, Member member, String chatMessage, Date chatRegDate, int chatReadCheck) {
        this.chatRoom = chatRoom;
        this.member = member;
        this.chatMessage = chatMessage;
        this.chatRegDate = chatRegDate;
        this.chatReadCheck = chatReadCheck;
    }
}
