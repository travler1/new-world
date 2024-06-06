package myproject.domain.member;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public void join(SaveMemberForm SaveMemberForm) {

        log.info("회원가입 서비스 호출: {}", SaveMemberForm);

        Member member = new Member();

        member.createMember(SaveMemberForm.getUsername(),
                SaveMemberForm.getPassword(),
                SaveMemberForm.getPhone(),
                SaveMemberForm.getEmail(),
                new Address(SaveMemberForm.getZipcode(),
                SaveMemberForm.getAddress1(),
                SaveMemberForm.getAddress2()));

        memberRepository.save(member);
    }

    //테이블 id로 회원 찾기
    public Member findMemberById(Long id) {
        return memberRepository.findMemberById(id);
    }


    public UploadFile findUploadFileById(Long id) {
        return memberRepository.findUploadFileById(id);
    }

    public Optional<Member> getMemberByEmail(String email) {
        return memberRepository.findMemberByEmail(email);
    }

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
