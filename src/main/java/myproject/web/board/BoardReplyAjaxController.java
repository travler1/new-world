package myproject.web.board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.domain.board.Board;
import myproject.domain.board.BoardReply;
import myproject.domain.board.boardReply.BoardReplyService;
import myproject.domain.board.BoardService;
import myproject.domain.member.EmbeddedDate;
import myproject.domain.member.Member;
import myproject.domain.member.MemberService;
import myproject.web.member.MemberDTO.SessionMemberForm;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

    //세션에 로그인된 회원의 아이디 조회 메서드
    private Long getLoginMemberId(HttpSession session) {
        SessionMemberForm loginMember = (SessionMemberForm) session.getAttribute("loginMember");
        if (loginMember == null) {
            throw new IllegalStateException("로그인 정보가 없습니다.");
        }
        return loginMember.getId();
    }

    /*================================
     *      	   댓글 등록
     *===============================*/
    @PostMapping("writeReply")
    public Map<String, Object> writeReply(HttpSession session, HttpServletRequest request,
                             @ModelAttribute SaveBoardReplyForm saveBoardReplyForm) {

        log.info("댓글 등록 ={}", saveBoardReplyForm);

        Board board = boardService.getBoard(saveBoardReplyForm.getBoardId());
        Member member = memberService.findMemberById(getLoginMemberId(session));

        BoardReply boardReply = new BoardReply().builder()
                .content(saveBoardReplyForm.getContent())
                .board(board)
                .date(new EmbeddedDate(new Date(), null))
                .ip(request.getRemoteAddr())
                .member(member)
                .build();

        log.info("댓글 등록={}", boardReply);
        boardReplyService.register(boardReply);

        Map<String, Object> result = new HashMap<>();
        result.put("result", "success");

        return result;
    }

    /*================================
     *      	   댓글 목록
     *===============================*/
    @PostMapping("/listReply")
    public Map<String, Object> listReply(HttpSession session,
                                         @RequestParam("boardId") Long boardId,
                                         Model model) {

        log.info("댓글 목록 진입, boardId={}", boardId);
        Map<String, Object> result = new HashMap<>();
        Long count = boardReplyService.getBoardReplyCount(boardId);
        List<ReadBoardReplyForm> readBoardReplyFormList = boardReplyService.getReadBoardReplyFormList(boardId);


        result.put("result", "success");
        result.put("count", count);
        result.put("list", readBoardReplyFormList);
        result.put("start", null);
        result.put("end", null);
        result.put("memberId", getLoginMemberId(session));
        model.addAttribute("pageNum", 1);

        return result;
    }

    /*================================
     *      	   댓글 수정
     *===============================*/
    @PostMapping("updateReply")
    public Map<String, Object> modifyReply(HttpSession session,
                                           HttpServletRequest request,
                                           @ModelAttribute UpdateReplyForm updateReplyForm) {

        boardReplyService.updateBoardReply(updateReplyForm, request.getRemoteAddr(), new Date());
        Map<String, Object> result = new HashMap<>();
        result.put("result", "success");

        return result;
    }

    /*================================
     *      	   댓글 삭제
     *===============================*/
    @PostMapping("deleteReply")
    public Map<String, Object> deleteReply(HttpSession session,
                                           @RequestParam("boardReplyId") Long boardReplyId) {

        Map<String, Object> result = new HashMap<>();
        BoardReply boardReply = boardReplyService.getBoardReply(boardReplyId);

        if (boardReply.getMember().getId() != getLoginMemberId(session)) {
            result.put("result", "wrongAccess");
        }
        boardReplyService.deleteBoardReply(boardReplyId);
        result.put("result", "success");
        return result;
    }

}
