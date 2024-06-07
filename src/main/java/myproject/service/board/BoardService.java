package myproject.service.board;

import myproject.domain.board.Board;
import myproject.web.board.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface BoardService {

    Long register(SaveBoardForm saveBoardForm, String ip, Long loginMemberId) throws IOException;

    ReadBoardForm readBoardDetail(Long id);

    Board getBoard(Long boardId);

    public Page<ListBoardForm> searchBoardList(BoardSearchCondition condition, Pageable pageable);

    EditBoardForm getEditBoardFormById(Long boardId);

    Long edit(EditBoardForm editBoardForm, String ip, Long loginMemberId) throws IOException;

    void deleteBoard(Long id);

    Page<ListBoardForm> getBoardListByLoginMember(Long loginMemberId, Pageable pageable);
}
