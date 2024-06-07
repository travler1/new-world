package myproject.domain.board;

import myproject.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {


    Board findBoardById(Long boardId);
}
