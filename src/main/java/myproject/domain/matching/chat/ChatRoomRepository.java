package myproject.domain.matching.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("select cr.id from ChatRoom cr where cr.sender.id =:senderId and cr.receiver.id =:receiverId")
    Optional<Long> findChatRoomByMember(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);

    ChatRoom findChatRoomById(Long id);
}
