package myproject.web.member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.service.member.MemberService;
import myproject.web.member.MemberDTO.SaveMemberForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

import static myproject.Util.commonResultAlert;

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
                         Model model, HttpServletRequest request,
                         RedirectAttributes redirectAttributes) {

        //유효성 체크 결과 오류가 있으면 폼 호출
        if (bindingResult.hasErrors()) {
            log.info("검증오류 발생 errors : {}", bindingResult);
            return "template/home/memberRegister";
        }

        //전송된 데이터 회원가입 처리
        memberService.join(saveMemberForm);
        log.info("회원가입 성공 처리, saveMemberForm : {}", saveMemberForm);

        commonResultAlert("회원가입이 완료되었습니다.", "/", redirectAttributes, request);

        return "redirect:/noNeedLogin/resultAlert";
    }

    /*=========================
       이메일 중복체크 (Ajax통신)
     *=========================*/
    @PostMapping("/members/register/confirmEmail")
    @ResponseBody
    public Map<String,Object> memberRegisterConfirmEmail(@RequestParam String email) {


        //이메일 중복체크 메서드 실행
        Map<String, Object> result = memberService.checkEmailDuplicated(email);
        log.info("이메일 중복체크 메서드 실행, email: {}", email);

        return result;
    }

}
