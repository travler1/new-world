package myproject.domain.matching;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpSession;
import myproject.web.file.UploadFile;
import myproject.web.matching.EmpEditForm;
import myproject.web.matching.EmpRegisterForm;
import myproject.web.matching.advice.SendAdviceForm;
import myproject.web.member.MemberDTO.SessionMemberForm;

import java.io.IOException;
import java.util.List;

public interface MatchingService {

    //Emp등록
    public void registerEmp(EmpRegisterForm empRegisterForm, SessionMemberForm sessionMemberForm) throws IOException;

    //나의 취업등록 정보 조회
    EmpEditForm findMyEmp(SessionMemberForm sessionMemberForm);

    //다른 현직자의 취업등록 정보 조회
    EmpEditForm findEmpByMemberId(Long id);

    UploadFile findUploadFileById(Long id);

    //취업현황 지도에 표시할 맵 데이터
    String jsonEmpMapProfileDtoList() throws JsonProcessingException;

    //취업현황 프로필에 표시할 멤버id리스트
    List<Long> empMemberIdList();

    //취업현황 통계에 사용할 Emp리스트
    String jsonEmpInfoTop100List() throws JsonProcessingException;


}
