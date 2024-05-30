package myproject.domain.matching.chat;

import myproject.web.matching.chat.ChatDto;

import java.util.Map;

public interface ChatService {

    //채팅방 생성
    Long findChatRoomByMember(Long senderId, Long receiverId);

    Map<String, Object> chatDetailResult(Long chatRoomId, Long loginMemberId);

    void insertChat(ChatDto chatDto, Long chatRoom_num, Long MemberId);
}
