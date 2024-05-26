package myproject.web.matching;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.domain.matching.EmpInfo;
import myproject.domain.matching.MatchingService;
import myproject.web.file.FileCategory;
import myproject.web.file.FileStore;
import myproject.web.file.UploadFile;
import myproject.web.member.MemberDTO.SessionMemberForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/matching")
@Transactional
public class MatchingController {

    private final FileStore fileStore;
    private final MatchingService matchingService;

    @GetMapping()
    public String matching() {
        return "template/matching/main";
    }

    //수강생 취업 정보 등록 폼 호출
    @GetMapping("/emp/register")
    public String empRegisterForm(@ModelAttribute("form") EmpRegisterForm empRegisterForm,
                                  HttpSession session, Model model, HttpServletRequest request) {
        //회원 접근이 아닐 시 로그인 화면으로 이동
        loginCheck(session, model, request);

        return "template/matching/emp_register";
    }

    //수강생 취업 정보 등록
    @PostMapping("/emp/register")
    public String empRegister(@Valid @ModelAttribute("form") EmpRegisterForm empRegisterForm,
                              BindingResult bindingResult, Model model, HttpSession session,
                              HttpServletRequest request) throws IOException {

        log.info("EmpRegisterForm={}", empRegisterForm);

        //유효성 체크 결과 오류가 있으면 폼 호출
        if (bindingResult.hasErrors()) {
            log.info("emp등록 검증오류 발생 error = {}", bindingResult);
            return "template/matching/emp_register";
        }

        log.info("EmpRegisterForm={}", empRegisterForm);

        //전송된 데이터 저장 처리
        SessionMemberForm loginMember = (SessionMemberForm) session.getAttribute("loginMember");

        matchingService.registerEmp(empRegisterForm, loginMember);

        log.info("emp 등록 성공");

        //View에 표시할 메시지
        model.addAttribute("message", "글쓰기가 완료되었습니다.");
        model.addAttribute("url", request.getContextPath() + "/matching");

        return "template/matching/resultAlert";
    }

    //내 취업정보 확인
    @GetMapping("/my_emp_register")
    public String my_emp_register(HttpSession session, Model model) {

        //현재 로그인된 회원 조회
        SessionMemberForm sessionMemberForm = (SessionMemberForm) session.getAttribute("loginMember");
        EmpEditForm empRegisterForm = matchingService.findMyEmp(sessionMemberForm);

        model.addAttribute("form", empRegisterForm);
        log.info("로그인 취업 등록 정보 ={}", empRegisterForm.toString());
        return "template/matching/my_emp_register";
    }

    //emp 등록 폼 전송 객체 생성 메서드
    private String loginCheck(HttpSession session, Model model, HttpServletRequest request) {
        if (session.getAttribute("loginMember") == null) {
            //View에 표시할 메시지
            model.addAttribute("message", "수강생 회원만 이용가능합니다.");
            model.addAttribute("url", request.getContextPath() + "/main/login");

            return "matching/resultAlert";
        }
        return null;
    }
}
