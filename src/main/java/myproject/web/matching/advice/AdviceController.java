package myproject.web.matching.advice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.LoginAccount;
import myproject.web.matching.advice.dto.AdviceSearchCondition;
import myproject.service.matching.advice.AdviceService;
import myproject.domain.member.Member;
import myproject.service.member.MemberService;
import myproject.web.matching.advice.dto.ListAdviceForm;
import myproject.web.matching.advice.dto.ReadAdviceForm;
import myproject.web.matching.advice.dto.SendAdviceForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

import static myproject.Util.commonResultAlert;
import static myproject.Util.templatePagingInfo;

@Controller
@RequiredArgsConstructor
@Slf4j
@Transactional
//@RequestMapping("/matching")
public class AdviceController {

    private final MemberService memberService;
    private final AdviceService adviceService;

    //첨삭 폼 호출
    @GetMapping(value = {"/matching/send_advice", "/myPage/send_advice"})
    public String send_advice(@LoginAccount Member member,
                              @RequestParam(value = "memberId", required = false) Long id,
                              RedirectAttributes redirectAttributes,
                              HttpServletRequest request,
                              Model model, @ModelAttribute("form") SendAdviceForm form) {

        if(member == null) {
            commonResultAlert("로그인한 회원만 이용 가능합니다.", "/loginForm", redirectAttributes, request);
            return "redirect:/common/childResultAlert";
        }

        model.addAttribute("login_user", member);
        model.addAttribute("receive_user", memberService.findMemberById(id));

        return "template/matching/advice/send_advice";
    }

    //첨삭 저장
    @PostMapping(value = {"/matching/send_advice", "/myPage/myAdvice/send_advice"})
    public String saveSend_advice(@Valid @ModelAttribute("form") SendAdviceForm form,
                                  BindingResult bindingResult, HttpSession session, Model model,
                                  @RequestParam("sender") Long sender_id,
                                  @RequestParam("receiver") Long receiver_id,
                                  @RequestParam(value = "adviceId", required = false) Long adviceId,
                                  RedirectAttributes redirectAttributes,
                                  HttpServletRequest request) throws IOException {

        if (bindingResult.hasErrors()) {
            return "template/matching/advice/send_advice";
        }

        //첨삭 저장
        adviceService.saveAdviceForm(form, request.getRemoteAddr(), adviceId);
        //PGR
        commonResultAlert("첨삭 요청이 성공적으로 완료되었습니다.", request.getContextPath() + "/matching", redirectAttributes,request);

        return "redirect:/common/resultAlert";
    }

    //내가 받은 첨삭 조회
    @GetMapping("/myPage/myAdvice")
    public String myAdvice(@RequestParam(value = "keyfield", required = false) Integer keyfield,
                           @RequestParam(value = "keyword", required = false) String keyword,
                           @LoginAccount Member member, Model model,
                           @PageableDefault(page = 1) Pageable pageable) {

        AdviceSearchCondition condition = new AdviceSearchCondition(keyfield, keyword);
        Page<ListAdviceForm> list = adviceService.getListReceivedAdvice(condition, pageable, member.getId());
        //내가 받은 첨삭 페이징 + 검색 처리
        templatePagingInfo(model, pageable, list, 5);

        log.info("첨삭보관함 list={}, content={}", list.getTotalElements(), list.getContent());

        return "template/myPage/myAdvice/receivedAdvice";
    }

    //내가 보낸 첨삭 조회
    @GetMapping("/myPage/myAdviceSent")
    public String myAdviceSent(@RequestParam(value = "keyfield", required = false) Integer keyfield,
                               @RequestParam(value = "keyword", required = false) String keyword,
                               @LoginAccount Member member, Model model,
                               @PageableDefault(page = 1) Pageable pageable) {

        AdviceSearchCondition condition = new AdviceSearchCondition(keyfield, keyword);
        Page<ListAdviceForm> list = adviceService.getListSentAdvice(condition, pageable, member.getId());

        //내가 보낸 첨삭 페이징 + 검색 처리
        templatePagingInfo(model, pageable, list, 5);

        log.info("첨삭보관함 list={}, content={}", list.getTotalElements(), list.getContent());

        return "template/myPage/myAdvice/sentAdvice";
    }

    //첨삭 단건 조회
    @GetMapping(path = {"/myPage/myAdvice/{id}", "/myPage/myAdviceSent/{id}"})
    public String adviceDetail(@PathVariable("id") Long id,
                               HttpSession session, Model model) {

        ReadAdviceForm readAdviceForm = adviceService.getAdvice(id);
        model.addAttribute("advice", readAdviceForm);

        return "template/myPage/myAdvice/adviceDetail";
    }

    //첨삭 답장 폼 호출
    @GetMapping("/myPage/myAdvice/send_advice")
    public String adviceSend_advice(@LoginAccount Member member, Model model,
                                    @RequestParam("adviceId") Long adviceId,
                                    @ModelAttribute("form") SendAdviceForm form) {

        //답장 폼 셋팅 후 출력(받는사람, 보내는사람)
        SendAdviceForm sendAdviceForm = adviceService.respondAdvice(adviceId, member.getId());

        model.addAttribute("login_user", member);
        model.addAttribute("receive_user", memberService.findMemberById(sendAdviceForm.getReceiver()));

        log.info("login_user ={}, receiver_user={}, form={}", member, memberService.findMemberById(sendAdviceForm.getReceiver()), sendAdviceForm);

        return "template/matching/advice/send_advice";
    }
}
