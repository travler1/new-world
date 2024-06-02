package myproject.domain.board;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import myproject.domain.member.QMember;
import myproject.web.board.BoardListDto;
import myproject.web.board.QBoardListDto;
import myproject.web.board.QReadBoardForm;

import java.util.List;

import static myproject.domain.board.QBoard.board;
import static myproject.domain.board.QBoardFav.boardFav;
import static myproject.domain.board.QBoardReply.boardReply;
import static myproject.domain.member.QMember.member;

public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {

    private JPAQueryFactory jpaQueryFactory;

    public BoardRepositoryCustomImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    public List<BoardListDto> boardListDtos() {
        List<BoardListDto> boardListDtos = jpaQueryFactory.select(new QBoardListDto(
                        board.id,
                        board.title,
                        member.username,
                        board.date.reg_date,
                        board.hit,
                        boardFav.countDistinct(),
                        boardReply.countDistinct()
                )).from(board)
                .leftJoin(board.member, member)
                .leftJoin(board.boardFavList, boardFav)
                .leftJoin(board.boardReplyList, boardReply)
                .groupBy(board.id, board.title, member.username, board.date.reg_date, board.hit)
                .fetch();

        return boardListDtos;
    }

    //글 조회 시 조회수 증가
    @Override
    public void updateboardHit(Long id) {
        jpaQueryFactory.update(board)
                .set(board.hit, board.hit.add(1))
                .where(board.id.eq(id))
                .execute();
    }

    @Override
    public Board findBoardById(Long id) {

        jpaQueryFactory.select(new QReadBoardForm(
                board.id,
                board.title,
                board.content,
                board.hit,
                member.id,
                board.date,
                board.uploadFile,
                board.ip,
                boardFav.countDistinct(),
                boardReply
        )).from(board)
                .leftJoin(board.boardFavList, boardFav)
                .leftJoin(board.boardReplyList, boardReply).fetch()
                .leftJoin(board.member, member)
                .where(board.id.eq(id))
                .fetchOne();

        return null;
    }
}