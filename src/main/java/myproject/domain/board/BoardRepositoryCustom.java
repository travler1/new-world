package myproject.domain.board;

import myproject.web.board.BoardListDto;
import myproject.web.board.BoardSearchCondition;
import myproject.web.board.ListBoardForm;
import myproject.web.board.ReadBoardForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardRepositoryCustom {

    List<BoardListDto> boardListDtos();

    void updateboardHit(Long id);

    //Board Id로 board 찾기
    ReadBoardForm findReadBoardFormById(Long id);

    Page<BoardListDto> search(BoardSearchCondition boardSearchCondition, Pageable pageable);

    void update(Long id, Board editBoard);

    Page<ListBoardForm> findBoardListByMemberId(Long loginMemberId, Pageable pageable);
}
