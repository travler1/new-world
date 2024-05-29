package myproject.domain.matching;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import myproject.domain.member.Member;

import java.util.List;
import java.util.stream.Collectors;

import static myproject.domain.matching.QEmpInfo.*;
import static myproject.domain.member.QMember.member;


@Slf4j
public class MatchingRepositoryCustomImpl implements MatchingRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;
    public MatchingRepositoryCustomImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public EmpInfo findEmpInfo(Member member) {

        return  jpaQueryFactory.select(empInfo)
                .from(empInfo)
                .join(empInfo.member).fetchJoin()
                .where(empInfo.member.eq(member))
                .fetchOne();
    }

    @Override
    public List<EmpMapProfileDto> findEmpInfoTop1000() {

        List<EmpMapProfileDto> empMapProfileDtoList = jpaQueryFactory.select(Projections.constructor(EmpMapProfileDto.class,
                        empInfo.member, empInfo.location_api_lat, empInfo.location_api_lng))
                .from(empInfo)
                .orderBy(empInfo.date.reg_date.desc())
                .limit(1000)
                .fetch();

        return empMapProfileDtoList;
    }

    @Override
    public List<Long> findEmpMemberIdTop1000() {

        List<Long> resultList = jpaQueryFactory.select(member.id)
                .from(member)
                .join(member.empInfo)
                .limit(1000)
                .fetch();

        return resultList;
    }

    @Override
    public EmpInfo findEmpInfoByMemberId(Long id) {

        return jpaQueryFactory.select(empInfo)
                .from(empInfo)
                .join(empInfo.member)
                .where(empInfo.member.id.eq(id))
                .fetchOne();

    }

    @Override
    public List<EmpInfo> findEmpInfoTop100() {

        List<EmpInfo> empInfoList = jpaQueryFactory.select(empInfo)
                .from(empInfo)
                .limit(1000)
                .fetch();

        return empInfoList;
    }
}
