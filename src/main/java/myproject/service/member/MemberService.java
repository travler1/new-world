package myproject.service.member;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.domain.member.Address;
import myproject.domain.member.Member;
import myproject.domain.member.MemberRepository;
import myproject.web.file.UploadFile;
import myproject.web.member.MemberDTO.SaveMemberForm;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    //회원가입
    public void join(SaveMemberForm saveMemberForm) {

        log.info("회원가입 서비스 호출: {}", saveMemberForm);

        Member saveMember = saveMemberForm.toEntity();

        log.info("saveMember = {}", saveMember);

        /*Member member = new Member();

        member.createMember(saveMemberForm.getUsername(),
                saveMemberForm.getPassword(),
                saveMemberForm.getPhone(),
                saveMemberForm.getEmail(),
                new Address(saveMemberForm.getZipcode(),
                saveMemberForm.getAddress1(),
                saveMemberForm.getAddress2()));*/

        memberRepository.save(saveMember);
    }

    //테이블 id로 회원 찾기
    public Member findMemberById(Long id) {
        return memberRepository.findMemberById(id);
    }

    //업로드 파일 찾기
    public UploadFile findUploadFileById(Long id) {
        return memberRepository.findUploadFileById(id);
    }

    //회원가입 - 이메일 중복체크(기존회원체크)
    public Optional<Member> getMemberByEmail(String email) {
        return memberRepository.findMemberByEmail(email);
    }

    //회원가입 - 이메일 중복체크
    public Map<String, Object> checkEmailDuplicated(String email) {
        Map<String, Object> result = new HashMap<>();

        Optional<Member> memberByEmail = getMemberByEmail(email);

        if(memberByEmail.isPresent()) {
            result.put("result", "idDuplicated");
        }else{
            if (!Pattern.matches("\\w+@\\w.\\w+(\\.\\w+)?", email)) {
                result.put("result", "notMatchPattern");
            }else{
                result.put("result","idNotFound");
            }
        }
        return result;
    }
}
