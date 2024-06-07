package myproject.domain.board.boardReply;

import myproject.web.board.dto.boardReplyDto.ReadBoardReplyForm;

import java.util.Date;
import java.util.List;

public interface BoardReplyCustom {

    //게시판 댓글 리스트 조회
    List<ReadBoardReplyForm> findReadBoardReplyFormList(Long boardId);

    //게시판 댓글 수정
    void updateBoardReply(Long boardReplyId, String boardReplyContent, String ip, Date modify_date);
}
