package myproject.web.board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.LoginAccount;
import myproject.domain.board.entity.BoardReply;
import myproject.service.board.boardReply.BoardReplyService;
import myproject.service.board.BoardService;
import myproject.domain.member.Member;
import myproject.service.member.MemberService;
import myproject.web.board.dto.boardReplyDto.SaveBoardReplyForm;
import myproject.web.board.dto.boardReplyDto.UpdateReplyForm;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Transactional
@Slf4j
@RequestMapping("/board")
public class BoardReplyAjaxController {

    private final BoardService boardService;
    private final BoardReplyService boardReplyService;
    private final MemberService memberService;

    /*================================
     *      	   댓글 등록
     *===============================*/
    @PostMapping("writeReply")
    public Map<String, Object> writeReply(@LoginAccount Member member, HttpServletRequest request,
                                          @ModelAttribute SaveBoardReplyForm saveBoardReplyForm) {

        log.info("댓글 등록 ={}", saveBoardReplyForm);

        String ip = request.getRemoteAddr();
        //댓글 저장
        BoardReply savedBoardReply = boardReplyService.save(saveBoardReplyForm, ip, member.getId());
        //댓글 저장 결과 ajaxMap으로 변환
        Map<String, Object> result = boardReplyService.register(savedBoardReply);

        return result;
    }

    /*================================
     *      	   댓글 목록
     *===============================*/
    @PostMapping("/listReply")
    public Map<String, Object> listReply(@LoginAccount Member member,
                                         @RequestParam("boardId") Long boardId,
                                         Model model) {

        log.info("댓글 목록 진입, boardId={}", boardId);

        Map<String, Object> result = boardReplyService.listBoardReplyForm(boardId, member.getId());

        return result;
    }

    /*================================
     *      	   댓글 수정
     *===============================*/
    @PostMapping("updateReply")
    public Map<String, Object> modifyReply(HttpSession session,
                                           HttpServletRequest request,
                                           @ModelAttribute UpdateReplyForm updateReplyForm) {

        String ip = request.getRemoteAddr();
        //댓글 업데이트
        Long updateBoardReplyId = boardReplyService.update(updateReplyForm, ip);
        //댓글 업데이트 결과 ajax 처리
        Map<String, Object> result = boardReplyService.updateAjaxResult(updateBoardReplyId);

        return result;
    }

    /*================================
     *      	   댓글 삭제
     *===============================*/
    @PostMapping("deleteReply")
    public Map<String, Object> deleteReply(@LoginAccount Member member,
                                           @RequestParam("boardReplyId") Long boardReplyId) {

        //댓글 등록 memberId와 로그인memberId 비교 후 삭제처리
        Map<String, Object> result = boardReplyService.deleteAjaxResult(boardReplyId, member.getId());

        return result;
    }

}
