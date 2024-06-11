package myproject.web.config.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.domain.member.Grade;
import myproject.domain.member.Member;
import myproject.domain.member.repository.MemberRepository;
import myproject.web.config.auth.PrincipalDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final MemberRepository memberRepository;

    //후처리되는 함수
    //구글로부터 받은 userRequest 데이터에 대한 후처리되는 함수
    //함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("getClientRegistration ={}", userRequest.getClientRegistration());  //registrationId로 어떤 OAuth로 로그인했는지 확인가능
        log.info("getAccessToken={}", userRequest.getAccessToken().getTokenValue()); //지금은 별로 안중요

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("getAttribute={}", oAuth2User.getAttributes());

        //구글로그인 버튼 클릭 -> 구글로그인창 -> 로그인을 완료 -> code를 리턴(OAuth-Client 라이브러리) -> AccessToken요청
        //userRequest정보 -> loadUser함수 호출 -> 구글로부터 회원프로필 받아준다.
        String provider = userRequest.getClientRegistration().getClientId(); //google
        String providerId = oAuth2User.getAttribute("sub");
        String name = provider + "_" + providerId;
        String username = oAuth2User.getAttribute("name");
        String password = bCryptPasswordEncoder.encode("겟인데어");
        String email = oAuth2User.getAttribute("email");
        Grade grade = Grade.STUDENT;

        Optional<Member> memberOptional = memberRepository.findMemberByName(name);
        Member member;

        if (memberOptional.isPresent()) {
            log.info("이미 소셜 로그인 가입된 회원입니다. member={}", memberOptional.get());
            member = memberOptional.get();
        } else {
            member = Member.builder()
                    .provider(provider)
                    .providerId(providerId)
                    .name(name)
                    .username(username)
                    .password(password)
                    .email(email)
                    .grade(grade)
                    .build();
            Member saveMember = memberRepository.save(member);
            log.info("소셜로그인 가입, 회원정보={}", saveMember);
        }

        log.info("소셜로그인 가입, user={}", oAuth2User.getAttributes());

        //회원가입을 강제로 진행
        return new PrincipalDetails(member, oAuth2User.getAttributes());

    }


}

