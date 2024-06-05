package myproject.domain.board;

import myproject.web.board.BoardListDto;
import myproject.web.board.BoardSearchCondition;
import myproject.web.board.ReadBoardForm;
import myproject.web.board.SaveBoardForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface BoardService {

    void register(SaveBoardForm saveBoardForm, String ip, Long loginMemberId) throws IOException;

    List<BoardListDto> boardListDto();

    ReadBoardForm readBoardDetail(Long id);

    Optional<BoardFav> getFav(Long boardId, Long memberId);

    Long getFavCount(Long boardId);

    void deleteBoardFav(Long boardFavId);

    void registerFav(Long boardId, Long memberId);

    Board getBoard(Long boardId);

    public Page<BoardListDto> searchBoards(BoardSearchCondition condition, Pageable pageable);
}
