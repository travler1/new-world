package myproject.web.board;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.LoginAccount;
import myproject.domain.board.entity.BoardFav;
import myproject.domain.member.Member;
import myproject.service.board.BoardService;
import myproject.service.board.boardFav.BoardFavService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Transactional
@Slf4j
@RequestMapping("/board")
public class BoardFavAjaxController {

    private final BoardService boardService;
    private final BoardFavService boardFavService;

    /*================================
     *  	  부모글 좋아요 읽기
     *  좋아요 유무에 따른 하트 표시 출력
     *===============================*/
    @PostMapping("/getFav")
    public Map<String, Object> getFav(@RequestParam("boardId") Long boardId,
                                      @LoginAccount Member member) {

        //부모글 좋아요 조회
        Optional<BoardFav> findFav = boardFavService.getFav(boardId, member.getId());
        //부모글 좋아요에 따른 결과 출력
        Map<String, Object> boardFav = boardFavService.getBoardFavAjaxResult(findFav, boardId);
        log.info("findFav: {}", findFav);

        return boardFav;
    }

    /*================================
     *      부모글 좋아요 등록/삭제
     *===============================*/
    @PostMapping("/writeFav")
    public Map<String, Object> writeFav(@LoginAccount Member member,
                                        @RequestParam("boardId") Long boardId){
        //부모글 좋아요 조회
        Optional<BoardFav> getFav = boardFavService.getFav(boardId, member.getId());
        //부모글 좋아요 조회에 따른 결과 출력. 좋아요존재->좋아요삭제, 좋아요존재X->좋아요등록
        Map<String, Object> result = boardFavService.doFavAjaxResult(getFav, boardId, member.getId());

        return result;
    }
}
