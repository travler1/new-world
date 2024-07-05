package myproject.web.home;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.LoginAccount;
import myproject.domain.member.Member;
import myproject.service.member.MemberService;
import myproject.web.config.auth.PrincipalDetails;
import myproject.web.member.MemberDTO.SessionMemberForm;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberService memberService;

    //메인페이지 호출
    @GetMapping("/")
    public String home() {

        //애플리케이션 개발시 자동 로그인 처리
       /* Member memberById = memberService.findMemberById(1L);
        SessionMemberForm sessionMemberForm = new SessionMemberForm(memberById.getId(), memberById.getUsername(), memberById.getEmail(), memberById.getProfileImage(), memberById.getGrade());
        HttpSession session = request.getSession();*/
        //session.setAttribute("loginMember", sessionMemberForm);

        return "template/home/home";
    }

    @GetMapping("/logged")
    public String loggedHome(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {

        model.addAttribute("member", principalDetails.getMember());

        return "template/home/home";
    }

    //공통 페이지 호출 (PRG)
    @GetMapping(value = {"/common/resultAlert", "/noNeedLogin/resultAlert"})
    public String resultAlert(HttpServletRequest request) {
        return "template/common/resultAlert";
    }

    //공통 페이지 호출 (PRG) - 새 창의 경우 기존 창을 닫고, 부모창으로 이동
    @GetMapping("common/childResultAlert")
    public String childResultAlert(HttpServletRequest request) {
        return "template/common/childResultAlert";
    }

    @GetMapping("/common/resultView")
    public String resultView(HttpServletRequest request) {
        return "template/common/resultView";
    }
}
