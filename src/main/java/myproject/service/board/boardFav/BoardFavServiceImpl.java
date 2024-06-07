package myproject.service.board.boardFav;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.domain.board.Board;
import myproject.domain.board.BoardFav;
import myproject.domain.board.BoardRepository;
import myproject.domain.board.boardFav.BoardFavRepository;
import myproject.domain.member.Member;
import myproject.domain.member.repository.MemberRepository;
import myproject.service.member.MemberService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class BoardFavServiceImpl implements BoardFavService{

    private final BoardFavRepository boardFavRepository;
    private final MemberService memberService;
    private final BoardRepository boardRepository;

    //게시판 좋아요 유무 확인
    @Override
    public Optional<BoardFav> getFav(Long boardId, Long memberId) {
        Optional<BoardFav> findFav = boardFavRepository.findFav(boardId, memberId);
        return findFav;
    }

    //게시판 좋아요 유무에 따른 결과 출력
    @Override
    public Map<String, Object> getBoardFavAjaxResult(Optional<BoardFav> boardFav, Long boardId) {

        Map<String, Object> result = new HashMap<>();
        //부모글 좋아요 유무 확인

        if(boardFav.isPresent()) {
            result.put("status", "yesFav");
        }else{
            result.put("status", "noFav");
        }

        Long favCount = getFavCount(boardId);

        result.put("count", favCount);
        result.put("result", "success");

        return result;
    }

    //부모글 좋아요 등록 및 삭제 ajax 결과 출력
    @Override
    public Map<String, Object> doFavAjaxResult(Optional<BoardFav> boardFav, Long boardId, Long memberId) {

        Map<String, Object> result = new HashMap<>();

        if(boardFav.isPresent()) {
            deleteBoardFav(boardFav.get().getId());
            result.put("status", "noFav");
        }else{
            registerFav(boardId, memberId);
            result.put("status", "yesFav");
        }

        Long count = getFavCount(boardId);
        result.put("count", count);
        result.put("result", "success");

        return result;
    }

    //게시판 좋아요 갯수 출력
    @Override
    public Long getFavCount(Long boardId) {
        Long favCount = boardFavRepository.findFavCount(boardId);
        return favCount;
    }

    //좋아요 삭제
    @Override
    public void deleteBoardFav(Long boardFavId) {
        boardFavRepository.deleteBoardFavById(boardFavId);
    }

    //좋아요 등록
    @Override
    public void registerFav(Long boardId, Long memberId) {
        Board board = boardRepository.findBoardById(boardId);
        Member member = memberService.findMemberById(memberId);
        BoardFav boardFav = new BoardFav().builder()
                .board(board)
                .member(member)
                .build();
        boardFavRepository.save(boardFav);
    }
}
