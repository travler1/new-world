package myproject.domain.matching.chat;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChatRoomRepositoryCustomImpl implements ChatRoomRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    public ChatRoomRepositoryCustomImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }

}
