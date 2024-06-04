package myproject.web.board;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.domain.board.BoardFav;
import myproject.domain.board.BoardService;
import myproject.domain.member.MemberService;
import myproject.web.member.MemberDTO.SessionMemberForm;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Transactional
@Slf4j
@RequestMapping("/board")
public class BoardFavAjaxController {

    private final BoardService boardService;
    private final MemberService memberService;

    private Long getLoginMemberId(HttpSession session) {
        SessionMemberForm loginMember = (SessionMemberForm) session.getAttribute("loginMember");
        if (loginMember == null) {
            throw new IllegalStateException("로그인 정보가 없습니다.");
        }
        return loginMember.getId();
    }

    /*================================
     *  	  부모글 좋아요 읽기
     *  좋아요 유무에 따른 하트 표시 출력
     *===============================*/
    @PostMapping("/getFav")
    public Map<String, Object> getFav(@RequestParam("boardId") Long boardId,
                                      HttpSession session) {

        Map<String, Object> result = new HashMap<>();

        Optional<BoardFav> findFav = boardService.getFav(boardId, ((SessionMemberForm) session.getAttribute("loginMember")).getId());
        log.info("findFav: {}", findFav);
        if(findFav.isPresent()) {
            result.put("status", "yesFav");
        }else{
            result.put("status", "noFav");
        }

        Long count = boardService.getFavCount(boardId);
        result.put("count", count);
        result.put("result", "success");
        return result;
    }

    /*================================
     *      부모글 좋아요 등록/삭제
     *===============================*/
    @PostMapping("/writeFav")
    public Map<String, Object> writeFav(HttpSession session,
                                        @RequestParam("boardId") Long boardId){
        Map<String, Object> result = new HashMap<>();

        Long memberId =((SessionMemberForm)session.getAttribute("loginMember")).getId();

        //이전에 등록했는지 확인
        Optional<BoardFav> getFav = boardService.getFav(boardId, getLoginMemberId(session));
        if(getFav.isPresent()) {
            boardService.deleteBoardFav(getFav.get().getId());
            result.put("status", "noFav");
        }else{
            boardService.registerFav(boardId, getLoginMemberId(session));
            result.put("status", "yesFav");
        }

        Long count = boardService.getFavCount(boardId);
        result.put("count", count);
        result.put("result", "success");

        return result;
    }
}
