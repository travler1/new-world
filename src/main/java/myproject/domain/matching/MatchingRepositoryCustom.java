package myproject.domain.matching;

import myproject.domain.member.Member;

import java.util.List;

public interface MatchingRepositoryCustom {

    public EmpInfo findEmpInfo(Member member);

    public EmpInfo findEmpInfoByMemberId(Long id);

    public List<EmpMapProfileDto> findEmpInfoTop1000();

    public List<Long> findEmpMemberIdTop1000();

    public List<EmpInfo> findEmpInfoTop100();

}
