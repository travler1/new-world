package myproject.domain.matching;

import jakarta.servlet.http.HttpSession;
import myproject.web.matching.EmpEditForm;
import myproject.web.matching.EmpRegisterForm;
import myproject.web.member.MemberDTO.SessionMemberForm;

import java.io.IOException;

public interface MatchingService {

    //Emp등록
    public void registerEmp(EmpRegisterForm empRegisterForm, SessionMemberForm sessionMemberForm) throws IOException;

    EmpEditForm findMyEmp(SessionMemberForm sessionMemberForm);
}
