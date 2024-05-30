package myproject.domain.matching.chat;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import myproject.web.matching.chat.ChatDto;
import org.springframework.stereotype.Repository;

import java.util.List;

import static myproject.domain.matching.chat.QChat.*;
import static myproject.domain.matching.chat.QChatRoom.chatRoom;

@Repository
public class ChatRepositoryCustomImpl implements ChatRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    public ChatRepositoryCustomImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<ChatDto> findChatListByChatRoomId(Long chatRoomId) {

        List<ChatDto> chatDtoList = jpaQueryFactory.select(Projections.constructor(
                        ChatDto.class, chat.id, chat.chatRoom.id, chat.member.id,
                        chat.member.username, chat.chatMessage, chat.chatRegDate,
                        chat.chatReadCheck
                ))
                .from(chat)
                .join(chat.chatRoom)
                .join(chat.member)
                .where(chat.chatRoom.id.eq(chatRoomId))
                .orderBy(chat.chatRegDate.asc())
                .fetch();

        return chatDtoList;
    }
}
