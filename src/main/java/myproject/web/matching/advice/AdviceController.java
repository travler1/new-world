package myproject.web.matching.advice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.domain.matching.MatchingService;
import myproject.domain.matching.advice.AdviceSearchCondition;
import myproject.domain.matching.advice.AdviceService;
import myproject.domain.member.MemberService;
import myproject.web.member.MemberDTO.SessionMemberForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    //세션에 로그인된 회원의 아이디 조회 메서드
    private Long getLoginMemberId(HttpSession session) {
        SessionMemberForm loginMember = (SessionMemberForm) session.getAttribute("loginMember");
        if (loginMember == null) {
            throw new IllegalStateException("로그인 정보가 없습니다.");
        }
        return loginMember.getId();
    }

    //첨삭 폼 호출
    @GetMapping(value = {"/matching/send_advice", "/myPage/send_advice"})
    public String send_advice(HttpSession session, @RequestParam("memberId") Long id,
                              Model model, @ModelAttribute("form") SendAdviceForm form) {

        model.addAttribute("login_user", session.getAttribute("loginMember"));
        model.addAttribute("receive_user", memberService.findMemberById(id));

        return "template/matching/advice/send_advice";
    }

    //첨삭 저장
    @PostMapping(value = {"/matching/send_advice", "/myPage/myAdvice/send_advice"})
    public String saveSend_advice(@Valid @ModelAttribute("form") SendAdviceForm form,
                                  BindingResult bindingResult, HttpSession session, Model model,
                                  @RequestParam("sender") Long sender_id,
                                  @RequestParam("receiver") Long receiver_id,
                                  @RequestParam(value = "adviceId", required = false) Long advice_id,
                                  HttpServletRequest request) throws IOException {

        if (bindingResult.hasErrors()) {
            return "template/matching/advice/send_advice";
        }

        adviceService.saveAdviceForm(form, request.getRemoteAddr(), advice_id);

        model.addAttribute("message", "첨삭 요청이 성공적으로 전송되었습니다.");
        model.addAttribute("url", request.getContextPath() + "/matching");

        return "template/matching/advice/resultAlert";
    }

    //내가 받은 첨삭 조회
    @GetMapping("/myPage/myAdvice")
    public String myAdvice(@RequestParam(value = "keyfield", required = false) Integer keyfield,
                           @RequestParam(value = "keyword", required = false) String keyword,
                           HttpSession session, Model model,
                           @PageableDefault(page = 1) Pageable pageable) {

        AdviceSearchCondition condition = new AdviceSearchCondition(keyfield, keyword);
        Page<ListAdviceForm> list = adviceService.getListReceivedAdvice(condition, pageable, getLoginMemberId(session));

        /**
         * blockLimit : page 개수 설정
         * 현재 사용자가 선택한 페이지 앞 뒤로 3페이지씩만 보여준다.
         * ex : 현재 사용자가 4페이지라면 2, 3, (4), 5, 6
         */
        int blockLimit = 10;
        int startPage = (((int) Math.ceil(((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
        int endPage = Math.min((startPage + blockLimit - 1), list.getTotalPages());
        model.addAttribute("list", list);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        log.info("첨삭보관함 list={}, content={}", list.getTotalElements(), list.getContent());

        return "template/myPage/myAdvice/receivedAdvice";
    }

    //내가 보낸 첨삭 조회
    @GetMapping("/myPage/myAdviceSent")
    public String myAdviceSent(@RequestParam(value = "keyfield", required = false) Integer keyfield,
                               @RequestParam(value = "keyword", required = false) String keyword,
                               HttpSession session, Model model,
                               @PageableDefault(page = 1) Pageable pageable) {

        AdviceSearchCondition condition = new AdviceSearchCondition(keyfield, keyword);
        Page<ListAdviceForm> list = adviceService.getListSentAdvice(condition, pageable, getLoginMemberId(session));

        int blockLimit = 10;
        int startPage = (((int) Math.ceil(((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
        int endPage = Math.min((startPage + blockLimit - 1), list.getTotalPages());
        model.addAttribute("list", list);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

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
    public String adviceSend_advice(HttpSession session, Model model,
                                    @RequestParam("adviceId") Long adviceId) {

        //답장 폼 셋팅 후 출력(받는사람, 보내는사람)
        SendAdviceForm sendAdviceForm = adviceService.respondAdvice(adviceId, getLoginMemberId(session));
        model.addAttribute("login_user", session.getAttribute("loginMember"));
        model.addAttribute("receive_user", memberService.findMemberById(sendAdviceForm.getReceiver()));
        model.addAttribute("form", sendAdviceForm);

        log.info("login_user ={}, receiver_user={}, form={}", session.getAttribute("loginMember"), memberService.findMemberById(sendAdviceForm.getReceiver()), sendAdviceForm);

        return "template/matching/advice/send_advice";
    }
}
