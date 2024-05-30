package myproject.web.matching.chat;

import lombok.Data;
import myproject.domain.member.Member;

@Data
public class ChatRoomDto {

    private Member sender;
    private Member receiver;
}
