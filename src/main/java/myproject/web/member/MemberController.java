package myproject.web.member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.domain.member.MemberService;
import myproject.web.member.MemberDTO.SaveMemberForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MemberController {

    private final MemberService memberService;

    /*=========================
            회원가입 폼 호출
     *=========================*/
    @GetMapping("/members/register")
    public String register(@ModelAttribute SaveMemberForm saveMemberForm) {
        return "template/home/memberRegister";
    }

    /*=========================
             회원가입 로직
     *=========================*/
    @PostMapping("/members/register")
    public String submit(@Valid SaveMemberForm saveMemberForm,
                         BindingResult bindingResult,
                         Model model, HttpServletRequest request) {

        log.info("회원가입 폼 {}", saveMemberForm);

        //유효성 체크 결과 오류가 있으면 폼 호출
        if (bindingResult.hasErrors()) {
            log.info("검증오류 발생 errors : {}", bindingResult);
            return "template/home/memberRegister";
        }

        //전송된 데이터 회원가입 처리
        memberService.join(saveMemberForm);
        //회원가입 성공처리
        modelResult(model, request);

        log.info("회원가입 성공");
        return "redirect:/";
    }

    /*=========================
       이메일 중복처리 (Ajax통신)
     *=========================*/
    @PostMapping("/members/register/confirmEmail")
    @ResponseBody
    public Map<String,Object> memberRegisterConfirmEmail(@RequestParam String email) {
        
        log.info("이메일 중복체크 메서드 진입, email: {}", email);

        //이메일 중복체크 메서드 실행
        Map<String, Object> result = memberService.checkEmailDuplicated(email);

        return result;
    }

    private static void modelResult(Model model, HttpServletRequest request) {
        model.addAttribute("accessTitle", "회원가입");
        model.addAttribute("accessMsg", "회원가입이 완료되었습니다.");
        model.addAttribute("accessUrl", request.getContextPath() + "/");
    }
}
