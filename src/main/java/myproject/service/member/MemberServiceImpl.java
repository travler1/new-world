package myproject.service.member;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.domain.member.Member;
import myproject.domain.member.repository.MemberRepository;
import myproject.web.config.CustomBCryptoPasswordEncoder;
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
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final CustomBCryptoPasswordEncoder bCryptoPasswordEncoder;

    //회원가입
    public void join(SaveMemberForm saveMemberForm) {

        String rawPassword = saveMemberForm.getPassword();
        String encPassword = bCryptoPasswordEncoder.encode(rawPassword);
        saveMemberForm.setPassword(encPassword);

        //DTO->Entity 변환 후 저장
        Member saveMember = saveMemberForm.toEntity();

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
            if (!Pattern.matches("\\w+@\\w+\\.\\w+(\\.\\w+)?", email)) {
                result.put("result", "notMatchPattern");
            }else{
                result.put("result","idNotFound");
            }
        }
        return result;
    }

}
