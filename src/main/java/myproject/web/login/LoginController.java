package myproject.web.login;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.domain.login.LoginService;
import myproject.domain.member.Member;
import myproject.web.member.MemberDTO.SessionMemberForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static myproject.web.config.interceptor.LoginCheckInterceptor.LOGIN_MEMBER;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final LoginService loginService;

    //로그인 폼 호출
    @GetMapping("/loginForm")
    public String login(@ModelAttribute LoginForm loginForm, BindingResult bindingResult,
                        @RequestParam(required = false) String error,
                        @RequestParam(required = false) String errorMessage) {

        if("username".equals(error)){
            bindingResult.rejectValue("username", "Illegal", errorMessage);
        }else if("password".equals(error)){
            bindingResult.rejectValue("password", "Illegal", errorMessage);
        }else if("object".equals(error)){
            //전역 오류로 처리
            bindingResult.reject("objectError", errorMessage);
        }

        return "template/home/login";
    }


    //로그인 처리
    @PostMapping("/login")
    public String loginCheck(@Valid @ModelAttribute LoginForm loginForm,
                             BindingResult bindingResult, Model model,
                             HttpServletRequest request,
                             @RequestParam(defaultValue = "/") String redirectURL) {

        //로그인 폼 입력오류 시 로그인 폼 재호출
        if (bindingResult.hasErrors()) {
            return "template/home/login";
        }
        //로그인 폼 정보로 멤버 조회 후 최소정보만 세션에 저장(id, username, email, profileImage, grade)
        Member member = loginService.login(loginForm.getUsername(), loginForm.getPassword());

        if (member == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "template/home/login";
        }

        SessionMemberForm loginMember = new SessionMemberForm(member);

        //세션이 있으면 세션 반환, 없으면 신규 생성
        HttpSession session = request.getSession();
        //세션에 로그인 회원정보 저장
        session.setAttribute(LOGIN_MEMBER, loginMember);
        log.info("세션 로그인 회원 정보 {} " , loginMember);

        return "redirect:" + redirectURL;
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        //세션 조회 후 세션이 있을 시 세션삭제
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

}
