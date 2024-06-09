package myproject.domain.board.repository;

import myproject.domain.board.entity.Board;
import myproject.web.board.dto.boardDto.BoardSearchCondition;
import myproject.web.board.dto.boardDto.ListBoardForm;
import myproject.web.board.dto.boardDto.ReadBoardForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {

    void updateboardHit(Long id);

    //Board Id로 board 찾기
    ReadBoardForm findReadBoardFormById(Long id);

    Page<ListBoardForm> search(BoardSearchCondition boardSearchCondition, Pageable pageable);

    Long update(Long id, Board editBoard);

    Page<ListBoardForm> findBoardListByMemberId(Long loginMemberId, Pageable pageable);
}
