package myproject;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import myproject.service.member.MemberService;
import myproject.web.member.MemberDTO.SessionMemberForm;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static myproject.web.config.interceptor.LoginCheckInterceptor.LOGIN_MEMBER;

@Component
@RequiredArgsConstructor
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

        //세션 조회
        HttpSession session = webRequest.getNativeRequest(HttpServletRequest.class).getSession();
        //세션에 저장된 멤버의 아이디 조회
        Long memberId = ((SessionMemberForm) session.getAttribute(LOGIN_MEMBER)).getId();
        if (memberId == null) {
            throw new IllegalStateException("로그인 된 회원이 없습니다.");
        }
        //멤버 조회
        return memberService.findMemberById(memberId);
    }
}
