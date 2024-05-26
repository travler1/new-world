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

        log.info("회원가입 서비스 호출: {}", addMemberForm);

        Member member = new Member();

        member.createMember(addMemberForm.getUsername(),
                addMemberForm.getPassword(),
                addMemberForm.getPhone(),
                addMemberForm.getEmail(),
                new Address(addMemberForm.getZipcode(),
                addMemberForm.getAddress1(),
                addMemberForm.getAddress2()));

        memberRepository.save(member);
    }

    //테이블 id로 회원 찾기
    public Member findMemberById(Long id) {
        return memberRepository.findMemberById(id);
    }


}
