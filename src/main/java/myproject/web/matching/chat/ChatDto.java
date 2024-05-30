package myproject.web.matching.chat;

import lombok.Data;
import myproject.domain.matching.chat.ChatRoom;

import java.util.Date;

@Data
public class ChatDto {


    private Long id;

    private Long chatRoomId;

    private Long memberId;

    private String username;

    private String chatMessage;

    private Date chatRegDate;

    private Integer chatReadCheck;

    public ChatDto(Long id, Long chatRoomId, Long memberId, String username, String chatMessage, Date chatRegDate, Integer chatReadCheck) {
        this.id = id;
        this.chatRoomId = chatRoomId;
        this.memberId = memberId;
        this.username = username;
        this.chatMessage = chatMessage;
        this.chatRegDate = chatRegDate;
        this.chatReadCheck = chatReadCheck;
    }
}
