package myproject.domain.board.boardReply;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.domain.board.BoardReply;
import myproject.web.board.ReadBoardReplyForm;
import myproject.web.board.UpdateReplyForm;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class BoardReplyServiceImpl implements BoardReplyService {

    private final BoardReplyRepository boardReplyRepository;

    //게시글의 댓글 갯수
    @Override
    public Long getBoardReplyCount(Long boardId) {
        Long boardReplyCountByBoardId = boardReplyRepository.findBoardReplyCountByBoardId(boardId);
        return boardReplyCountByBoardId;
    }

    //게시글의 댓글 리스트
    @Override
    public List<ReadBoardReplyForm> getReadBoardReplyFormList(Long boardId) {
        List<ReadBoardReplyForm> readBoardReplyFormList = boardReplyRepository.findReadBoardReplyFormList(boardId);
        return readBoardReplyFormList;
    }

    //댓글 등록
    @Override
    public void register(BoardReply boardReply) {
        boardReplyRepository.save(boardReply);
    }

    //댓글 수정
    @Override
    public void updateBoardReply(UpdateReplyForm updateReplyForm, String ip, Date modify_date) {
        boardReplyRepository.updateBoardReply(
                updateReplyForm.getBoardReplyId(),
                updateReplyForm.getBoardReplyContent(),
                ip, new Date()
        );
    }

    //댓글 조회
    @Override
    public BoardReply getBoardReply(Long boardReplyId) {
        BoardReply boardReplyById = boardReplyRepository.findBoardReplyById(boardReplyId);
        return boardReplyById;
    }

    //댓글 삭제
    @Override
    public void deleteBoardReply(Long boardReplyId) {
        boardReplyRepository.deleteById(boardReplyId);
    }
}
