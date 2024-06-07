package myproject.domain.matching.emp;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import myproject.domain.member.Member;
import myproject.web.matching.emp.dto.*;


import java.util.List;

import static myproject.domain.matching.emp.QEmpInfo.empInfo;
import static myproject.domain.member.QMember.member;


@Slf4j
public class EmpRepositoryCustomImpl implements EmpRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public EmpRepositoryCustomImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public EmpInfo findEmpInfo(Member member) {

        return jpaQueryFactory.select(empInfo)
                .from(empInfo)
                .join(empInfo.member).fetchJoin()
                .where(empInfo.member.eq(member))
                .fetchOne();
    }

    @Override
    public List<EmpMapProfileForm> findEmpInfoTop1000() {

        List<EmpMapProfileForm> empMapProfileFormList = jpaQueryFactory.select(Projections.constructor(EmpMapProfileForm.class,
                        empInfo.member, empInfo.location_api_lat, empInfo.location_api_lng))
                .from(empInfo)
                .orderBy(empInfo.date.reg_date.desc())
                .limit(1000)
                .fetch();

        return empMapProfileFormList;
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

    //empInfo 취업정보 단건 조회
    @Override
    public ReadEmpForm findEmpInfoByMemberId(Long id) {

        return jpaQueryFactory.select(new QReadEmpForm(
                                empInfo.id,
                                empInfo.comSize,
                                empInfo.comPeople,
                                empInfo.field,
                                empInfo.role,
                                empInfo.career,
                                empInfo.salary_status,
                                empInfo.salary,
                                empInfo.periodTime,
                                empInfo.education,
                                empInfo.major,
                                empInfo.certification,
                                empInfo.location,
                                empInfo.location_api,
                                empInfo.location_api_lat,
                                empInfo.location_api_lng,
                                empInfo.workStart,
                                empInfo.advice,
                                empInfo.uploadFile,
                                empInfo.date,
                                empInfo.member
                        )
                ).from(empInfo)
                .join(empInfo.member)
                .where(empInfo.member.id.eq(id))
                .fetchOne();

    }

    //차트 출력을 위한 EmpInfo 정보 출력 (등록순으로 100개)
    @Override
    public List<EmpInfo> findEmpInfoTop100() {

        List<EmpInfo> empInfoList = jpaQueryFactory.select(empInfo)
                .from(empInfo)
                .limit(1000)
                .fetch();

        return empInfoList;
    }

    //차트 출력을 위한 EmpInfo 정보 출력 (등록순으로 100개)
    @Override
    public List<JsonCharEmpForm> findEmpCharInfoTop1000() {

        List<JsonCharEmpForm> jsonCharEmpFormList = jpaQueryFactory.select(new QJsonCharEmpForm(
                        empInfo.id,
                        empInfo.comSize,
                        empInfo.comPeople,
                        empInfo.field,
                        empInfo.role,
                        empInfo.career,
                        empInfo.salary_status,
                        empInfo.salary,
                        empInfo.periodTime,
                        empInfo.education,
                        empInfo.major,
                        empInfo.certification,
                        empInfo.location,
                        empInfo.location_api,
                        empInfo.location_api_lat,
                        empInfo.location_api_lng,
                        empInfo.workStart,
                        empInfo.advice
                )).from(empInfo)
                .orderBy(empInfo.date.reg_date.desc())
                .limit(1000)
                .fetch();

        return jsonCharEmpFormList;
    }
}
