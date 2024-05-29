package myproject.web.matching.advice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.domain.matching.MatchingService;
import myproject.domain.matching.advice.AdviceService;
import myproject.domain.member.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@Slf4j
@Transactional
//@RequestMapping("/matching")
public class AdviceController {

    private final MemberService memberService;
    private final AdviceService adviceService;

    @GetMapping(value = {"/matching/send_advice", "/myPage/send_advice"})
    public String send_advice(HttpSession session, @RequestParam("memberId") Long id,
                              Model model, @ModelAttribute("form") SendAdviceForm form) {

        model.addAttribute("login_user", session.getAttribute("loginMember"));
        model.addAttribute("receive_user", memberService.findMemberById(id));

        return "template/matching/advice/send_advice";
    }

    @PostMapping(value = {"/matching/send_advice", "/myPage/send_advice"})
    public String saveSend_advice(@Valid @ModelAttribute("form") SendAdviceForm form,
                                  BindingResult bindingResult, HttpSession session, Model model,
                                  @RequestParam("sender") Long sender_id,
                                  @RequestParam("receiver") Long receiver_id,
                                  HttpServletRequest request) throws IOException {

        if(bindingResult.hasErrors()) {
            return "template/matching/advice/send_advice";
        }

        adviceService.saveAdviceForm(form, request.getRemoteAddr());

        model.addAttribute("message", "첨삭 요청이 성공적으로 전송되었습니다.");
        model.addAttribute("url", request.getContextPath() + "/matching");

        return "template/matching/advice/resultAlert";
    }
}
