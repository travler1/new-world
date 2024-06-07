package myproject.web.home;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.domain.member.Member;
import myproject.service.member.MemberService;
import myproject.web.member.MemberDTO.SessionMemberForm;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberService memberService;

    //메인페이지 호출
    @GetMapping("/")
    public String home(HttpServletRequest request) {

        //애플리케이션 개발시 자동 로그인 처리
        Member memberById = memberService.findMemberById(1L);
        SessionMemberForm sessionMemberForm = new SessionMemberForm(memberById.getId(), memberById.getUsername(), memberById.getEmail(), memberById.getProfileImage(), memberById.getGrade());
        HttpSession session = request.getSession();
        session.setAttribute("loginMember", sessionMemberForm);
        return "template/home/home";
    }

    //공통 페이지 호출 (PRG)
    @GetMapping(value = {"/common/resultAlert", "/noNeedLogin/resultAlert"})
    public String resultAlert(HttpServletRequest request) {
        return "template/common/resultAlert";
    }

    @GetMapping("/common/resultView")
    public String resultView(HttpServletRequest request) {
        return "template/common/resultView";
    }
}
