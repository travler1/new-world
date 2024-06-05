package myproject.web.myPage;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.domain.board.BoardService;
import myproject.domain.member.Member;
import myproject.domain.member.MemberService;
import myproject.web.board.BoardListDto;
import myproject.web.board.BoardSearchCondition;
import myproject.web.board.ListBoardForm;
import myproject.web.member.MemberDTO.MyPageMemberForm;
import myproject.web.member.MemberDTO.SessionMemberForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MyPageController {

    private final MemberService memberService;
    private final BoardService boardService;

    //세션에 로그인된 회원의 아이디 조회 메서드
    private Long getLoginMemberId(HttpSession session) {
        SessionMemberForm loginMember = (SessionMemberForm) session.getAttribute("loginMember");
        if (loginMember == null) {
            throw new IllegalStateException("로그인 정보가 없습니다.");
        }
        return loginMember.getId();
    }

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

    /*==================================
     * 	  마이페이지 - 내가 작성한 글
     *=================================*/
    @GetMapping(path={"/myPage/board"})
    public String myBoard(Model model, HttpSession session, @PageableDefault(page=1) Pageable pageable) {


        Page<ListBoardForm> boardListByLoginMember = boardService.getBoardListByLoginMember(getLoginMemberId(session), pageable);


        /**
         * blockLimit : page 개수 설정
         * 현재 사용자가 선택한 페이지 앞 뒤로 3페이지씩만 보여준다.
         * ex : 현재 사용자가 4페이지라면 2, 3, (4), 5, 6
         */
        int blockLimit = 10;
        int startPage = (((int) Math.ceil(((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
        int endPage = Math.min((startPage + blockLimit - 1), boardListByLoginMember.getTotalPages());
        model.addAttribute("list", boardListByLoginMember);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "template/myPage/board";
    }
}
