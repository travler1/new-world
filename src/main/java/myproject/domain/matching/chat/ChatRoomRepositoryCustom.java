package myproject.domain.matching.chat;

import java.util.Optional;

public interface ChatRoomRepositoryCustom {

    Long findChatRoomByMember(Long senderId, Long receiverId);
}
