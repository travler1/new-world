package myproject.domain.board;

import myproject.web.board.BoardListDto;
import myproject.web.board.ReadBoardForm;

import java.util.List;

public interface BoardRepositoryCustom {

    List<BoardListDto> boardListDtos();

    void updateboardHit(Long id);

    //Board Id로 board 찾기
    ReadBoardForm findReadBoardFormById(Long id);
}
