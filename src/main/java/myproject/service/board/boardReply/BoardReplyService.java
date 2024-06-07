package myproject.service.board.boardReply;

import myproject.domain.board.entity.BoardReply;
import myproject.web.board.dto.boardReplyDto.ReadBoardReplyForm;
import myproject.web.board.dto.boardReplyDto.SaveBoardReplyForm;
import myproject.web.board.dto.boardReplyDto.UpdateReplyForm;

import java.util.List;
import java.util.Map;

public interface BoardReplyService {

    Long getBoardReplyCount(Long boardId);

    List<ReadBoardReplyForm> getReadBoardReplyFormList(Long boardId);

    //댓글 저장(SaveBoardReplyForm -> BoardReply 변환 후 저장)
    BoardReply save(SaveBoardReplyForm saveBoardReplyForm, String ip, Long memberId);

    //댓글 저장 후 ajax결과 처리
    Map<String, Object> register(BoardReply boardReply);

    Long update(UpdateReplyForm updateReplyForm, String ip);
    
    BoardReply getBoardReply(Long boardReplyId);

    void deleteBoardReply(Long boardReplyId);

    Map<String, Object> updateAjaxResult(Long updateBoardReplyId);

    Map<String, Object> deleteAjaxResult(Long boardReplyId, Long memberid);

    //부모글의 댓글 리스트 처리
    Map<String, Object> listBoardReplyForm(Long boardId, Long memberId);
}
