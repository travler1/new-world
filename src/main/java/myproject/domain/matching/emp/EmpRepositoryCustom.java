package myproject.domain.matching.emp;

import myproject.domain.member.Member;
import myproject.web.matching.emp.dto.EmpMapProfileForm;
import myproject.web.matching.emp.dto.JsonCharEmpForm;
import myproject.web.matching.emp.dto.ReadEmpForm;

import java.util.List;

public interface EmpRepositoryCustom {

    public EmpInfo findEmpInfo(Member member);

    public ReadEmpForm findEmpInfoByMemberId(Long id);

    public List<EmpMapProfileForm> findEmpInfoTop1000();

    public List<Long> findEmpMemberIdTop1000();

    public List<EmpInfo> findEmpInfoTop100();

    public List<JsonCharEmpForm> findEmpCharInfoTop1000();

}
