package myproject.service.board.boardFav;

import myproject.domain.board.entity.BoardFav;

import java.util.Map;
import java.util.Optional;

public interface BoardFavService {

    //게시판 좋아요 우무확인
    public Optional<BoardFav> getFav(Long boardId, Long memberId);

    //부모글 좋아요 유무 ajax결과 출력
    public Map<String, Object> getBoardFavAjaxResult(Optional<BoardFav> boardFav, Long boardId);

    //부모글 좋아요 등록 및 삭제 ajax 결과 출력
    public Map<String, Object> doFavAjaxResult(Optional<BoardFav> boardFav, Long boardId, Long memberId);

    //게시판 좋아요 갯수출력
    public Long getFavCount(Long boardId);

    //게시판 좋아요 삭제
    public void deleteBoardFav(Long boardFavId);

    //게시판 좋아요 등록
    public void registerFav(Long boardId, Long memberId);
}
