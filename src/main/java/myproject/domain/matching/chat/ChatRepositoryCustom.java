package myproject.domain.matching.chat;

import myproject.web.matching.chat.ChatDto;

import java.util.List;

public interface ChatRepositoryCustom {

    List<ChatDto> findChatListByChatRoomId(Long chatRoomId);

    void updateReadMessage(Long optionalChatRoomId, Long receiverId);
}
