package myproject.domain.board.boardReply;

import myproject.domain.member.EmbeddedDate;
import myproject.web.board.dto.boardReplyDto.ReadBoardReplyForm;

import java.util.List;

public interface BoardReplyRepositoryCustom {

    //게시판 댓글 리스트 조회
    List<ReadBoardReplyForm> findReadBoardReplyFormList(Long boardId);

    //게시판 댓글 수정
    Long updateBoardReply(Long boardReplyId, String boardReplyContent, String ip, EmbeddedDate date);
}
