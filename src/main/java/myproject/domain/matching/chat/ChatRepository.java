package myproject.domain.matching.chat;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long>, ChatRepositoryCustom {

}
