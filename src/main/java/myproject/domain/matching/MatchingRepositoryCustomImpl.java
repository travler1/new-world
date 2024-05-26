package myproject.domain.matching;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import myproject.domain.member.Member;
import myproject.domain.member.QMember;
import org.springframework.stereotype.Repository;

import static myproject.domain.matching.QEmpInfo.empInfo;
import static myproject.domain.member.QMember.member;

@Slf4j
public class MatchingRepositoryCustomImpl implements MatchingRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;
    public MatchingRepositoryCustomImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public EmpInfo findEmpInfo(Member member) {

        EmpInfo empInfo = jpaQueryFactory.select(QEmpInfo.empInfo)
                .from(QEmpInfo.empInfo)
                .join(QEmpInfo.empInfo.member).fetchJoin()
                .where(QEmpInfo.empInfo.member.eq(member))
                .fetchOne();

        return empInfo;
    }
}
