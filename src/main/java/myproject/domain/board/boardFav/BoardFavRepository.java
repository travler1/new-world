package myproject.domain.board.boardFav;

import myproject.domain.board.entity.BoardFav;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardFavRepository extends JpaRepository<BoardFav, Long> , BoardFavRepositoryCusom{

    //게시판 좋아요 유무 확인
    @Query("select bf from BoardFav bf where bf.board.id =:boardId and bf.member.id =:memberId")
    Optional<BoardFav> findFav(@Param("boardId") Long boardId, @Param("memberId") Long memberId);

    //게시판 좋아요 수 구하기
    @Query("select count(bf) from BoardFav bf where bf.board.id=:boardId")
    Long findFavCount(@Param("boardId") Long boardId);

    //게시판 좋아요 삭제
    @Query("delete from BoardFav bf where bf.id=:boardFavId")
    void removeBoardFavById(@Param("boardFavId") Long boardFavId);

}
