package myproject.domain.board.boardReply;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import myproject.domain.board.QBoardReply;
import myproject.web.board.QReadBoardReplyForm;
import myproject.web.board.ReadBoardForm;
import myproject.web.board.ReadBoardReplyForm;

import java.util.Date;
import java.util.List;

import static myproject.domain.board.QBoardReply.boardReply;

public class BoardReplyCustomImpl implements BoardReplyCustom{

    private JPAQueryFactory jpaQueryFactory;
    public BoardReplyCustomImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    //게시판 댓글 리스트 조회
    public List<ReadBoardReplyForm> findReadBoardReplyFormList(Long boardId){
        List<ReadBoardReplyForm> readBoardReplyFormList = jpaQueryFactory.select(new QReadBoardReplyForm(
                        boardReply.id,
                        boardReply.content,
                        boardReply.board.id,
                        boardReply.member.id,
                        boardReply.member.username,
                        boardReply.date.reg_date,
                        boardReply.date.modify_date
                ))
                .from(boardReply)
                .leftJoin(boardReply.member)
                .where(boardReply.board.id.eq(boardId))
                .fetch();

        return readBoardReplyFormList;
    }

    //게시판 댓글 수정

    @Override
    public void updateBoardReply(Long boardReplyId, String boardReplyContent, String ip, Date modify_date) {
        jpaQueryFactory.update(boardReply)
                .set(boardReply.content, boardReplyContent)
                .set(boardReply.ip, ip)
                .set(boardReply.date.modify_date, modify_date)
                .where(boardReply.id.eq(boardReplyId))
                .execute();
    }
}
