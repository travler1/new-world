package myproject.service.matching;

import com.fasterxml.jackson.core.JsonProcessingException;
import myproject.domain.member.Member;
import myproject.web.file.UploadFile;
import myproject.web.matching.emp.dto.EditEmpForm;
import myproject.web.matching.emp.dto.ReadEmpForm;
import myproject.web.matching.emp.dto.SaveEmpForm;
import myproject.web.member.MemberDTO.SessionMemberForm;

import java.io.IOException;
import java.util.List;

public interface EmpService {

    //Emp등록
    public void registerEmp(SaveEmpForm saveEmpForm, Member member) throws IOException;

    //나의 취업등록 정보 조회
    //ReadEmpForm findMyEmp(Long id);

    //다른 현직자의 취업등록 정보 조회
    ReadEmpForm findEmpByMemberId(Long id);

    UploadFile findUploadFileById(Long id);

    //취업현황 지도에 표시할 맵 데이터
    String jsonEmpMapProfileDtoList() throws JsonProcessingException;

    //취업현황 프로필에 표시할 멤버id리스트
    List<Long> empMemberIdList();

    //취업현황 통계에 사용할 Emp리스트
    String jsonEmpInfoTop100List() throws JsonProcessingException;


}
