package myproject.web.config.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.domain.member.Member;
import myproject.domain.member.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

//시큐리티 설정에서 loginProcessingUrl("/login");
// /login요청이 오면 자동으로 UserDetailsService 타입으로 IoC 되어 있는
//loadUserByUsername 함수가 실행. (규칙임) //중요! form 태그에서 username 이란 이름으로 받아야함.
//다른거로 받으려면 securityConfig에서 설정을 해줘야함. 웬만하면 username으로 쓰기..
@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    //시큐리티 session => Authentication(내부에 UserDetails가 들어감) = UserDetails
    //함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("일반회원 로그인 진입, username={}", username);
        Optional<Member> memberByEmail = memberRepository.findMemberByEmail(username);
        if (memberByEmail.isPresent()) {
            log.info("memberByEmail: {}", memberByEmail.get().getUsername());
            return new PrincipalDetails(memberByEmail.get());
        }
        return null;
    }
}
