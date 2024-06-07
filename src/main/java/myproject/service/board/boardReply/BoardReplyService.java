package myproject.service.board.boardReply;

import myproject.domain.board.BoardReply;
import myproject.web.board.dto.boardReplyDto.ReadBoardReplyForm;
import myproject.web.board.dto.boardReplyDto.SaveBoardReplyForm;
import myproject.web.board.dto.boardReplyDto.UpdateReplyForm;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface BoardReplyService {

    Long getBoardReplyCount(Long boardId);

    List<ReadBoardReplyForm> getReadBoardReplyFormList(Long boardId);

    //댓글 저장(SaveBoardReplyForm -> BoardReply 변환 후 저장)
    BoardReply save(SaveBoardReplyForm saveBoardReplyForm, String ip, Long memberId);

    //댓글 저장 후 ajax결과 처리
    Map<String, Object> register(BoardReply boardReply);

    void updateBoardReply(UpdateReplyForm updateReplyForm, String ip, Date modify_date);
    
    BoardReply getBoardReply(Long boardReplyId);

    void deleteBoardReply(Long boardReplyId);


}
