package myproject.web.matching.emp;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.LoginAccount;
import myproject.domain.member.Member;
import myproject.service.matching.EmpService;
import myproject.service.member.MemberService;
import myproject.web.file.FileStore;
import myproject.web.matching.emp.dto.ReadEmpForm;
import myproject.web.matching.emp.dto.SaveEmpForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

import static myproject.Util.commonResultAlert;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/matching")
@Transactional
public class EmpController {

    private final FileStore fileStore;
    private final EmpService empService;
    private final MemberService memberService;

    @GetMapping()
    public String matching(Model model, @LoginAccount Member member)
            throws JsonProcessingException {

        log.info("취업현황 페이지 진입={}");

        //지도에 프로필 출력
        model.addAttribute("jsonMapData", empService.jsonEmpMapProfileFormTop1000List());

        //스와이퍼에 프로필 출력
        model.addAttribute("empMemberIdList", empService.empMemberIdList());

        //통계자료에 활용할 자료 출력
        model.addAttribute("jsonEmpList", empService.jsonChartEmpFormTop1000List());


        return "template/matching/main";
    }

    //수강생 취업 정보 등록 폼 호출
    @GetMapping("/emp/register")
    public String empRegisterForm(@ModelAttribute("form") SaveEmpForm saveEmpForm,
                                  HttpSession session, Model model, HttpServletRequest request) {
        //수강생이 아닐 시 로그인 화면으로 이동
        loginCheck(session, model, request);

        return "template/matching/emp_register";
    }

    //수강생 취업 정보 등록
    @PostMapping("/emp/register")
    public String empRegister(@Valid @ModelAttribute("form") SaveEmpForm saveEmpForm,
                              BindingResult bindingResult, Model model, @LoginAccount Member member,
                              HttpServletRequest request,
                              RedirectAttributes redirectAttributes) throws IOException {

        log.info("EmpRegisterForm={}", saveEmpForm);

        //유효성 체크 결과 오류가 있으면 폼 호출
        if (bindingResult.hasErrors()) {
            log.info("emp등록 검증오류 발생 error = {}", bindingResult);
            return "template/matching/emp_register";
        }
        //전송된 데이터 저장 처리
        empService.registerEmp(saveEmpForm, member);

        log.info("emp 등록 성공");

        //View에 표시할 메시지

        commonResultAlert("취업정보 등록이 완료되었습니다.", request.getContextPath() + "/matching", redirectAttributes, request);

        return "redirect:/common/childResultAlert";
    }

    //내 취업정보 확인
    @GetMapping("/my_emp_register")
    public String my_emp_register(@LoginAccount Member member, Model model) {

        //현재 로그인된 회원 조회
        log.info("로그인 취업 등록 정보 ={}", member);
        ReadEmpForm empByMemberId = empService.findEmpByMemberId(member.getId());
        model.addAttribute("form", empByMemberId);

        return "template/matching/my_emp_register";
    }

    //다른 취업자 취업정보 확인
    @GetMapping("/see_emp_register")
    public String see_emp_register(Model model, @RequestParam("memberId") Long id) {

        ReadEmpForm empByMemberId = empService.findEmpByMemberId(id);
        model.addAttribute("form", empByMemberId);
        //다른 취업자 취업 정보 확인
        log.info("조회하고자 하는 취업자 취업 등록 정보 ={}", empByMemberId);

        return "template/matching/see_emp_register";
    }

    //emp 등록 폼 전송 객체 생성 메서드
    private String loginCheck(HttpSession session, Model model, HttpServletRequest request) {
        if (session.getAttribute("loginMember") == null) {
            //View에 표시할 메시지
            model.addAttribute("message", "수강생 회원만 이용가능합니다.");
            model.addAttribute("url", request.getContextPath() + "/main/login");

            return "redirect:/common/resultAlert";
        }
        return null;
    }
}
