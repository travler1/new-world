package myproject;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import myproject.domain.member.Member;
import myproject.service.member.MemberService;
import myproject.web.member.MemberDTO.SessionMemberForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//공용으로 사용할 메서드 모음
@RequiredArgsConstructor
public class Util {

    private final MemberService memberService;


    //세션에 로그인된 회원 인덱스 조회 메서드
    /*public static Long getLoginMemberId(HttpSession session) {
        SessionMemberForm loginMember = (SessionMemberForm) session.getAttribute("loginMember");
        if (loginMember == null) {
            throw new IllegalStateException("로그인 정보가 없습니다.");
        }
        return loginMember.getId();
    }
    //세션에 로그인된 회원 Member 객체 조회 메서드
    public static Member getLoginMember(HttpSession session, MemberService memberService) {
        Member member = memberService.findMemberById(getLoginMemberId(session));
        return member;
    }*/

     //템플릿으로 페이징 정보 전달
    public static<T> void templatePagingInfo(Model model, Pageable pageable, Page<T> list, int blockLimit) {

        /**
         * blockLimit : page 개수 설정
         * 현재 사용자가 선택한 페이지 앞 뒤로 3페이지씩만 보여준다.
         * ex : 현재 사용자가 4페이지라면 2, 3, (4), 5, 6
         */

        int startPage = (((int) Math.ceil(((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
        int endPage = Math.min((startPage + blockLimit - 1), list.getTotalPages());

        model.addAttribute("list", list);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
    }

    //common resultView
    public static void commonResultView(String accessTitle, String accessMsg, String accessUrl,
                                        RedirectAttributes redirectAttributes,
                                        HttpServletRequest request) {
        //addFlashAttribute는 리다이렉트 요청 후 한 번만 사용할 수 있는 데이터를 세션에 저장.
        redirectAttributes.addFlashAttribute("accessTitle", "Hello-world 요청 처리 완료");
        redirectAttributes.addFlashAttribute("accessMsg", accessMsg);
        redirectAttributes.addFlashAttribute("accessUrl", accessUrl);
    }

    //common resultAlert
    public static void commonResultAlert(String message, String url,
                                         RedirectAttributes redirectAttributes,
                                         HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("message", message);
        redirectAttributes.addFlashAttribute("url", request.getContextPath() + url);
    }
}
