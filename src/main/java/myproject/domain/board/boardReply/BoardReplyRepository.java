package myproject.domain.board.boardReply;

import myproject.domain.board.entity.BoardReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardReplyRepository extends JpaRepository<BoardReply, Long>, BoardReplyRepositoryCustom {

    @Query("select count(br) from BoardReply br where br.board.id=:boardId")
    Long findBoardReplyCountByBoardId(@Param("boardId") Long boardId);

    @Query("select br from BoardReply br where br.board.id=:boardId")
    List<BoardReply> findBoardReplyListByBoardId(@Param("boardId") Long boardId);

    BoardReply findBoardReplyById(Long boardReplyId);

}
