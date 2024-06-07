package myproject.domain.board;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import myproject.web.board.*;
import myproject.web.board.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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

    //게시판 글 조회 (페이징+검색)
    @Override
    public Page<ListBoardForm> search(BoardSearchCondition condition, Pageable pageable) {

        List<ListBoardForm> listBoardForms = jpaQueryFactory.select(new QListBoardForm(
                        board.id,
                        board.title,
                        member.username,
                        board.date.reg_date,
                        board.hit,
                        board.boardFavList.size().longValue(),
                        board.boardReplyList.size().longValue()
                )).from(board)
                .leftJoin(board.member, member)
                .leftJoin(board.boardFavList, boardFav)
                .leftJoin(board.boardReplyList, boardReply)
                .where(titleEq(condition.getKeyword(), condition.getKeyfield()),
                        usernameEq(condition.getKeyword(), condition.getKeyfield()),
                        contentEq(condition.getKeyword(), condition.getKeyfield()),
                        titleContentEq(condition.getKeyword(), condition.getKeyfield()))
                .orderBy(getOrderSpecifier(condition.getOrder()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .select(board.count())
                .from(board)
                .leftJoin(board.member, member)
                .where(titleEq(condition.getKeyword(), condition.getKeyfield()),
                        usernameEq(condition.getKeyword(), condition.getKeyfield()),
                        contentEq(condition.getKeyword(), condition.getKeyfield()),
                        titleContentEq(condition.getKeyword(), condition.getKeyfield()))
                .fetchOne();

        return new PageImpl<>(listBoardForms, pageable, total);
    }

    private Predicate titleEq(String keyword, Integer keyfield) {
        return keyword != null && keyfield == 1 ? board.title.like("%"+keyword+"%") : null;
    }

    private Predicate usernameEq(String keyword, Integer keyfield) {
        return keyword != null && keyfield == 2 ? member.username.like("%"+keyword+"%") : null;
    }

    private Predicate contentEq(String keyword, Integer keyfield) {
        return keyword != null && keyfield == 3 ? board.content.like("%"+keyword+"%") : null;
    }

    private Predicate titleContentEq(String keyword, Integer keyfield) {
        return keyword != null && keyfield == 4 ? board.title.like("%"+keyword+"%").or(
                board.content.like("%"+keyword+"%")
        ) : null;
    }

    private OrderSpecifier<?> getOrderSpecifier(Integer order) {
        if (order == null) {
            return board.date.reg_date.desc();
        }
        switch (order) {
            case 1:
                return board.date.reg_date.desc();
            case 2:
                return board.hit.desc();
            case 3:
                return board.boardFavList.size().desc();
            case 4:
                return board.boardReplyList.size().desc();
            default:
                return board.date.reg_date.desc();
        }
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
    public ReadBoardForm findReadBoardFormById(Long id) {

        ReadBoardForm readBoardForm = jpaQueryFactory.select(new QReadBoardForm(
                        board.id,
                        board.title,
                        board.content,
                        board.hit,
                        member.id,
                        member.username,
                        board.date,
                        board.uploadFile,
                        board.ip,
                        boardFav.countDistinct()
                )).from(board)
                .leftJoin(board.member, member)
                .leftJoin(board.boardFavList, boardFav)
                .where(board.id.eq(id))
                .groupBy(board.id, member.id)
                .fetchOne();

        //댓글 리스트는 따로 조회 후 병합
        List<BoardReply> boardReplyList = jpaQueryFactory.selectFrom(boardReply)
                .join(boardReply.board, board).fetchJoin()
                .where(board.id.eq(id))
                .fetch();

        readBoardForm.setBoardReplyList(boardReplyList);

        return readBoardForm;
    }

    //게시글 수정
    @Override
    public Long update(Long id, Board editBoard) {
        long updateBoardId = jpaQueryFactory.update(board)
                .set(board.title, editBoard.getTitle())
                .set(board.content, editBoard.getContent())
                .set(board.uploadFile, editBoard.getUploadFile())
                .set(board.ip, editBoard.getIp())
                .set(board.date, editBoard.getDate())
                .where(board.id.eq(id))
                .execute();

        return updateBoardId;
    }

    //마이페이지 내가 쓴 글 조회
    @Override
    public Page<ListBoardForm> findBoardListByMemberId(Long loginMemberId, Pageable pageable) {

        List<ListBoardForm> listBoardForms = jpaQueryFactory.select(new QListBoardForm(
                        board.id,
                        board.title,
                        member.username,
                        board.date.reg_date,
                        board.hit,
                        board.boardFavList.size().longValue(),
                        board.boardReplyList.size().longValue()
                )).from(board)
                .leftJoin(board.member, member)
                .where(board.member.id.eq(loginMemberId))
                .orderBy(board.date.reg_date.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory.select(board.count())
                .from(board)
                .leftJoin(board.member, member)
                .where(board.member.id.eq(loginMemberId))
                .orderBy(board.date.reg_date.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchOne();

        return new PageImpl<>(listBoardForms, pageable, total);
    }
}
