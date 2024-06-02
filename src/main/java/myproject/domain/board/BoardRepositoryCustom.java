package myproject.domain.board;

import myproject.web.board.BoardListDto;

import java.util.List;

public interface BoardRepositoryCustom {

    List<BoardListDto> boardListDtos();

    void updateboardHit(Long id);

    //Board Id로 board 찾기
    Board findBoardById(Long id);
}
