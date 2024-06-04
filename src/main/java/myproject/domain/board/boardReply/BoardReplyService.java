package myproject.domain.board.boardReply;

import myproject.domain.board.BoardReply;
import myproject.web.board.ReadBoardReplyForm;
import myproject.web.board.UpdateReplyForm;

import java.util.Date;
import java.util.List;

public interface BoardReplyService {

    Long getBoardReplyCount(Long boardId);

    List<ReadBoardReplyForm> getReadBoardReplyFormList(Long boardId);

    void register(BoardReply boardReply);

    void updateBoardReply(UpdateReplyForm updateReplyForm, String ip, Date modify_date);
    
    BoardReply getBoardReply(Long boardReplyId);

    void deleteBoardReply(Long boardReplyId);
}
