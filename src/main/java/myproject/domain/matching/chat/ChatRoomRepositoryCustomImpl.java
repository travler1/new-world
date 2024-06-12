package myproject.domain.matching.chat;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static myproject.domain.matching.chat.QChatRoom.chatRoom;

@Repository
@Slf4j
public class ChatRoomRepositoryCustomImpl implements ChatRoomRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    public ChatRoomRepositoryCustomImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Long findChatRoomByMember(Long senderId, Long receiverId) {

        List<Long> findChatRoomId = queryFactory.select(chatRoom.id)
                .from(chatRoom)
                .where(chatRoom.sender.id.eq(senderId).and(chatRoom.receiver.id.eq(receiverId)).or(chatRoom.receiver.id.eq(senderId).and(chatRoom.sender.id.eq(receiverId))))
                .fetch();
        log.info("senderId={}, receiverId={}", senderId, receiverId);
        log.info("findChatRoomList={}", findChatRoomId);

        if (!findChatRoomId.isEmpty()) {
            return findChatRoomId.get(0);
        }
        return null;
    }
}
