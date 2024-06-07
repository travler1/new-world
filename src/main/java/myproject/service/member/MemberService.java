package myproject.service.member;

import myproject.domain.member.Member;
import myproject.web.file.UploadFile;
import myproject.web.member.MemberDTO.SaveMemberForm;

import java.util.Map;
import java.util.Optional;

public interface MemberService {

    //회원가입
    public void join(SaveMemberForm saveMemberForm);

    //테이블 id로 회원 찾기
    public Member findMemberById(Long id);

    //업로드 파일 찾기
    public UploadFile findUploadFileById(Long id);

    //회원가입 - 이메일 중복체크(기존회원체크)
    public Optional<Member> getMemberByEmail(String email);

    //회원가입 - 이메일 중복체크
    public Map<String, Object> checkEmailDuplicated(String email);


}
