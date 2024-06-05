package myproject.domain.matching.advice;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import myproject.web.board.QReadBoardForm;
import myproject.web.matching.advice.ListAdviceForm;
import myproject.web.matching.advice.QListAdviceForm;
import myproject.web.matching.advice.QReadAdviceForm;
import myproject.web.matching.advice.ReadAdviceForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import static myproject.domain.matching.advice.QAdvice.advice;

@Slf4j
public class AdviceRepositoryCustomImpl implements AdviceRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public AdviceRepositoryCustomImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    //내가 받은 첨삭 조회
    @Override
    public Page<ListAdviceForm> findListReceivedAdviceById(AdviceSearchCondition condition, Pageable pageable, Long memberId) {

        List<ListAdviceForm> listAdviceForms = jpaQueryFactory.select(new QListAdviceForm(
                        advice.id,
                        advice.sender.username,
                        advice.date_sent,
                        advice.date_read,
                        advice.advice_complete
                )).from(advice)
                .leftJoin(advice.sender)
                .leftJoin(advice.receiver)
                .where(contentEq(condition.getKeyword(), condition.getKeyfield()))
                .where(advice.receiver.id.eq(memberId))
                .orderBy(advice.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory.select(advice.count())
                .from(advice)
                .leftJoin(advice.sender)
                .leftJoin(advice.receiver)
                .where(senderEq(condition.getKeyword(), condition.getKeyfield()),
                        contentEq(condition.getKeyword(), condition.getKeyfield()),
                        senderOrContentEq(condition.getKeyword(), condition.getKeyfield()))
                .where(advice.receiver.id.eq(memberId))
                .fetchOne();

        return new PageImpl<>(listAdviceForms, pageable, total);
    }

    //내가 보낸 첨삭 조회
    @Override
    public Page<ListAdviceForm> findListSentAdviceById(AdviceSearchCondition condition, Pageable pageable, Long memberId) {

        List<ListAdviceForm> listAdviceForms = jpaQueryFactory.select(new QListAdviceForm(
                        advice.id,
                        advice.receiver.username,
                        advice.date_sent,
                        advice.date_read,
                        advice.advice_complete
                )).from(advice)
                .leftJoin(advice.sender)
                .leftJoin(advice.receiver)
                .where(receiverEq(condition.getKeyword(), condition.getKeyfield()),
                        contentEq(condition.getKeyword(), condition.getKeyfield()),
                        receiverOrContentEq(condition.getKeyword(), condition.getKeyfield()))
                .where(advice.receiver.id.eq(memberId))
                .orderBy(advice.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory.select(advice.count())
                .from(advice)
                .leftJoin(advice.receiver)
                .leftJoin(advice.sender)
                .where(receiverEq(condition.getKeyword(), condition.getKeyfield()),
                        contentEq(condition.getKeyword(), condition.getKeyfield()),
                        receiverOrContentEq(condition.getKeyword(), condition.getKeyfield()))
                .where(advice.receiver.id.eq(memberId))
                .fetchOne();

        return new PageImpl<>(listAdviceForms, pageable, total);
    }

    private Predicate senderEq(String keyword, Integer keyfield) {
        return keyword != null && keyfield == 1 ? advice.sender.username.like("%" + keyword + "%") : null;
    }

    private Predicate contentEq(String keyword, Integer keyfield) {
        return keyword != null && keyfield == 2 ? advice.advice_content.like("%" + keyword + "%") : null;
    }

    private Predicate senderOrContentEq(String keyword, Integer keyfield) {
        return keyword != null && keyfield == 3 ?
                advice.sender.username.like("%" + keyword + "%")
                        .or(advice.advice_content.like("%" + keyword + "%")) : null;
    }

    private Predicate receiverEq(String keyword, Integer keyfield) {
        return keyword != null && keyfield == 1? advice.receiver.username.like("%" + keyword + "%") : null;
    }

    private Predicate receiverOrContentEq(String keyword, Integer keyfield) {
        return keyword != null && keyfield ==3?
                advice.sender.username.like("%"+keyword+"%")
                .or(advice.advice_content.like("%" + keyword + "%")) : null;
    }

    //첨삭 읽음 처리
    @Override
    public void updateRead(Long id, Date date) {
        jpaQueryFactory.update(advice)
                .set(advice.date_read, date)
                .where(advice.id.eq(id))
                .execute();
    }

    //첨삭 조회
    @Override
    public ReadAdviceForm findReadAdviceFormById(Long id) {

        ReadAdviceForm readAdviceForms = jpaQueryFactory.select(new QReadAdviceForm(
                        advice.id,
                        advice.receiver,
                        advice.sender,
                        advice.advice_content,
                        advice.date_sent,
                        advice.date_read,
                        advice.advice_complete,
                        advice.uploadFile
                ))
                .from(advice)
                .leftJoin(advice.sender)
                .leftJoin(advice.receiver)
                .where(advice.id.eq(id))
                .fetchOne();

        return readAdviceForms;
    }

    //첨삭 답장 완료 처리
    @Override
    public void updateAdviceComplete(Long adviceId) {
        jpaQueryFactory.update(advice)
                .set(advice.advice_complete, true)
                .where(advice.id.eq(adviceId))
                .execute();
    }

}
