package myproject.service.board.boardReply;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.domain.board.entity.Board;
import myproject.domain.board.entity.BoardReply;
import myproject.domain.board.boardReply.BoardReplyRepository;
import myproject.domain.member.EmbeddedDate;
import myproject.domain.member.Member;
import myproject.service.board.BoardService;
import myproject.service.member.MemberService;
import myproject.web.board.dto.boardReplyDto.ReadBoardReplyForm;
import myproject.web.board.dto.boardReplyDto.SaveBoardReplyForm;
import myproject.web.board.dto.boardReplyDto.UpdateReplyForm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class BoardReplyServiceImpl implements BoardReplyService {

    private final MemberService memberService;
    private final BoardReplyRepository boardReplyRepository;
    private final BoardService boardService;

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

    //게시글의 댓글 리스트 처리
    @Override
    public Map<String, Object> listBoardReplyForm(Long boardId, Long memberId) {

        Map<String, Object> result = new HashMap<>();

        Long count = getBoardReplyCount(boardId);
        List<ReadBoardReplyForm> readBoardReplyFormList = getReadBoardReplyFormList(boardId);

        result.put("result", "success");
        result.put("count", count);
        result.put("list", readBoardReplyFormList);
        result.put("start", null);
        result.put("end", null);
        result.put("memberId", memberId);

        return result;
    }

    //댓글 저장(SavedBoardReplyForm -> BoardReply)
    @Override
    public BoardReply save(SaveBoardReplyForm saveBoardReplyForm, String ip, Long memberId) {

        Member member = memberService.findMemberById(memberId);
        Board board = boardService.getBoardById(saveBoardReplyForm.getBoardId());

        BoardReply boardReply = new BoardReply().builder()
                .content(saveBoardReplyForm.getContent())
                .date(new EmbeddedDate(new Date(), null))
                .ip(ip)
                .board(board)
                .member(member)
                .build();

        BoardReply savedBoardReply = boardReplyRepository.save(boardReply);
        return savedBoardReply;
    }

    //댓글 저장 후 ajax 결과 처리
    @Override
    public Map<String, Object> register(BoardReply boardReply) {

        Map<String, Object> result = new HashMap<>();

        BoardReply boardReplyById = boardReplyRepository.findBoardReplyById(boardReply.getId());

        if (boardReplyById != null) {
            result.put("result", "success");
        }else{
            result.put("result", "error");
        }
        return result;
    }

    //댓글 수정
    @Override
    public Long update(UpdateReplyForm updateReplyForm, String ip) {

        BoardReply boardReplyById = boardReplyRepository.findBoardReplyById(updateReplyForm.getBoardReplyId());
        EmbeddedDate updateEmbeddedDate = new EmbeddedDate(boardReplyById.getDate().getReg_date(), new Date());

        Long updateBoardReplyId = boardReplyRepository.updateBoardReply(
                updateReplyForm.getBoardReplyId(),
                updateReplyForm.getBoardReplyContent(),
                ip,
                updateEmbeddedDate
        );

        return updateBoardReplyId;
    }

    //댓글 수정 후  ajax 결과 처리
    @Override
    public Map<String, Object> updateAjaxResult(Long updateBoardReplyId) {

        Map<String, Object> result = new HashMap<>();

        BoardReply boardReplyById = boardReplyRepository.findBoardReplyById(updateBoardReplyId);
        if (boardReplyById.getDate().getModify_date() != null) {
            result.put("result", "error");
        }else{
            result.put("result", "success");
        }

        return result;
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

    //댓글 삭제 ajax 결과 반환
    @Override
    public Map<String, Object> deleteAjaxResult(Long boardReplyId, Long memberId) {

        Map<String, Object> result = new HashMap<>();

        BoardReply boardReply = getBoardReply(boardReplyId);
        if (boardReply.getMember().getId() != memberId) {
            result.put("result", "wrongAccess");
        }else{
            deleteBoardReply(boardReplyId);
            result.put("result", "success");
        }

        return result;
    }
}
