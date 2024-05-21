package myproject.domain.member;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.web.member.MemberDTO.MyPageMemberForm;
import myproject.web.member.MemberDTO.SessionMemberForm;
import myproject.web.member.MemberDTO.addMemberForm;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    //회원가입
    public void join(addMemberForm addMemberForm) {

        if (memberRepository == null) {
            log.error("memberRepository가 주입되지 않았습니다.");
            return;
        }

        log.info("회원가입 서비스 호출: {}", addMemberForm);

        Member member = new Member();

        member.createMember(addMemberForm.getUsername(),
                addMemberForm.getPassword(),
                addMemberForm.getPhone(),
                addMemberForm.getEmail(),
                addMemberForm.getZipcode(),
                addMemberForm.getAddress1(),
                addMemberForm.getAddress2());

        memberRepository.save(member);
    }


    public Member findMemberById(Long id) {
        return memberRepository.findMemberById(id);
    }
}
