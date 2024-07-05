package myproject;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.domain.member.Member;
import myproject.service.member.MemberService;
import myproject.web.config.auth.PrincipalDetails;
import myproject.web.member.MemberDTO.SessionMemberForm;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static myproject.web.config.interceptor.LoginCheckInterceptor.LOGIN_MEMBER;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoginAccountArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(LoginAccount.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
                                throws Exception {


        // 시큐리티 컨텍스트에서 현재 인증된 사용자 정보 가져오기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 인증되지 않은 사용자 처리
        if (principal instanceof String && "anonymousUser".equals(principal)) {
            log.info("로그인 하지 않은 사용자로 접근");
            return null;
        }

        if (principal != null) {
            log.info("Principal found: {}", principal);
            PrincipalDetails principalDetails = (PrincipalDetails) principal;
            String email = principalDetails.getEmail();
            if (email == null) {
                throw new IllegalStateException("해당 이메일에 대한 회원 정보를 찾을 수 없습니다: " + email);
            }
            return memberService.getMemberByEmail(email).get();

        }else{
            //세션 조회
            HttpSession session = webRequest.getNativeRequest(HttpServletRequest.class).getSession();
            //세션에 저장된 멤버의 아이디 조회
            Long memberId = ((SessionMemberForm) session.getAttribute(LOGIN_MEMBER)).getId();
            if (memberId == null) {
                //throw new IllegalStateException("로그인 된 회원이 없습니다.");
                return null;
            }
            //멤버 조회
            return memberService.findMemberById(memberId);
        }
    }
}
