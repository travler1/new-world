package myproject.domain.board.boardFav;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import static myproject.domain.board.QBoardFav.*;

public class BoardFavRepositoryCustomImpl implements BoardFavRepositoryCusom {

    JPAQueryFactory queryFactory;

    public BoardFavRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public void deleteBoardFavById(Long boardFavId) {
        queryFactory.delete(boardFav)
                .where(boardFav.id.eq(boardFavId))
                .execute();
    }
}
