package myproject.domain.Login;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.domain.member.Member;
import myproject.domain.member.MemberRepository;
import myproject.web.member.MemberDTO.SessionMemberForm;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class LoginService {

    private final MemberRepository memberRepository;

    /**
     * @Return null이면 로그인 실패
     */
    public Member login(String email, String password) {
        Member member =  memberRepository.findMembersByEmailAndPassword(email,password).get();

        return member;
    }
}
