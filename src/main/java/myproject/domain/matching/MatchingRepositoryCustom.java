package myproject.domain.matching;

import myproject.domain.member.Member;

public interface MatchingRepositoryCustom {

    public EmpInfo findEmpInfo(Member member);
}
