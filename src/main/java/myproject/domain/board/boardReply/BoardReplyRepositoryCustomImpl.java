package myproject.domain.board.boardReply;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import myproject.domain.member.EmbeddedDate;
import myproject.web.board.dto.boardReplyDto.QReadBoardReplyForm;
import myproject.web.board.dto.boardReplyDto.ReadBoardReplyForm;

import java.util.List;

import static myproject.domain.board.entity.QBoardReply.boardReply;

public class BoardReplyRepositoryCustomImpl implements BoardReplyRepositoryCustom {

    private JPAQueryFactory jpaQueryFactory;
    public BoardReplyRepositoryCustomImpl(EntityManager em) {
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
    public Long updateBoardReply(Long boardReplyId, String boardReplyContent, String ip, EmbeddedDate date) {
        long updateBoardId = jpaQueryFactory.update(boardReply)
                .set(boardReply.content, boardReplyContent)
                .set(boardReply.ip, ip)
                .set(boardReply.date, date)
                .where(boardReply.id.eq(boardReplyId))
                .execute();

        return updateBoardId;
    }
}
