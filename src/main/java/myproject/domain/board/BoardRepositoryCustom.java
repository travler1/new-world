package myproject.domain.board;

import myproject.web.board.dto.BoardSearchCondition;
import myproject.web.board.dto.ListBoardForm;
import myproject.web.board.dto.ReadBoardForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardRepositoryCustom {

    void updateboardHit(Long id);

    //Board Id로 board 찾기
    ReadBoardForm findReadBoardFormById(Long id);

    Page<ListBoardForm> search(BoardSearchCondition boardSearchCondition, Pageable pageable);

    Long update(Long id, Board editBoard);

    Page<ListBoardForm> findBoardListByMemberId(Long loginMemberId, Pageable pageable);
}
