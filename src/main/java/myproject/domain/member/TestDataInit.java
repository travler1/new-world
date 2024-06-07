package myproject.domain.member;

import lombok.RequiredArgsConstructor;
import myproject.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final MemberRepository memberRepository;

    /**
     * 테스트용 데이터 추가
     */
    //@PostConstruct
    public void init() {

        Member member = new Member();
        member.createMember("테스트", "1234", null, "test@test.com", null);
        memberRepository.save(member);

    }
}
