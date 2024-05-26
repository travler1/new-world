package myproject.web.myPage;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.domain.member.Member;
import myproject.domain.member.MemberService;
import myproject.web.member.MemberDTO.MyPageMemberForm;
import myproject.web.member.MemberDTO.SessionMemberForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MyPageController {

    private final MemberService memberService;

    /*=========================
     *		    MY페이지
     *=========================*/
    @GetMapping("/members/myPage")
    public String myPage(HttpSession session, Model model) {

        //세션에서 로그인 회원 정보 조회
        SessionMemberForm loginMember = (SessionMemberForm) session.getAttribute("loginMember");
        Member memberEntity = memberService.findMemberById(loginMember.getId());

        //마이페이지 관리용 회원 상세 정보 조회
        MyPageMemberForm member = new MyPageMemberForm(memberEntity);

        log.info("회원 상세 정보 {}", member);

        model.addAttribute("member", member);

        return "template/myPage/myPage";
    }
}
