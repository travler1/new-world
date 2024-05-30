package myproject.domain.matching.chat;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import myproject.domain.member.Member;
import myproject.web.matching.chat.ChatRoomDto;

import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatRoom {

    @Id @GeneratedValue
    @Column(name = "chatRoom_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "senderId")
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "receiverId")
    private Member receiver;

    @OneToMany(mappedBy = "chatRoom")
    private List<Chat> chatList;

    //==연관관계 편의 메서드==//
    public void addChat(Chat chat) {
        chatList.add(chat);
        chat.setChatRoom(this);
    }

    public void removeChat(Chat chat) {
        chatList.remove(chat);
        chat.setChatRoom(null);
    }

    public List<Chat> getChatList() {
        return chatList;
    }

    public ChatRoom(Member sender, Member receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }
}
