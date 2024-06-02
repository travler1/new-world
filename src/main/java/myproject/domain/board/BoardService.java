package myproject.domain.board;

import myproject.web.board.BoardListDto;
import myproject.web.board.SaveBoardForm;

import java.io.IOException;
import java.util.List;

public interface BoardService {

    void register(SaveBoardForm saveBoardForm, String ip, Long loginMemberId) throws IOException;

    List<BoardListDto> boardListDto();

    Board readBoardDetail(Long id);
}
