package myproject.web.member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.domain.member.MemberService;
import myproject.web.member.MemberDTO.addMemberForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MemberController {

    private final MemberService memberService;

    /*=========================
     *회원가입
     *=========================*/

    //회원가입 폼 호출
    @GetMapping("/members/register")
    public String register(@ModelAttribute addMemberForm addMemberForm) {
        return "template/home/memberRegister";
    }

    //회원가입 메서드
    @PostMapping("/members/register")
    public String submit(@Valid addMemberForm addMemberForm, BindingResult bindingResult,
                         Model model, HttpServletRequest request) {

        log.info("회원가입 폼 {}", addMemberForm);

        //유효성 체크 결과 오류가 있으면 폼 호출
        if (bindingResult.hasErrors()) {
            log.info("검증오류 발생 errors : {}", bindingResult);
            return "template/home/memberRegister";
        }

        //회원가입
        if (memberService == null) {
            log.error("MemberService가 주입되지 않았습니다.");
            throw new IllegalStateException("MemberService가 주입되지 않았습니다.");
        }

        log.info("MemberService 나와라... {}", memberService);

        //전송된 데이터 회원가입 처리
        memberService.join(addMemberForm);

        model.addAttribute("accessTitle", "회원가입");
        model.addAttribute("accessMsg", "회원가입이 완료되었습니다.");
        model.addAttribute("accessUrl", request.getContextPath()+"/");
        
        log.info("회원가입 성공");
        return "redirect:/";
        //return "common/result";
    }
}
