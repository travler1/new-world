package myproject.web.myPage;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.service.board.BoardService;
import myproject.domain.member.Member;
import myproject.service.member.MemberService;
import myproject.web.board.dto.boardDto.ListBoardForm;
import myproject.web.member.MemberDTO.ReadMemberForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static myproject.Util.getLoginMemberId;
import static myproject.Util.templatePagingInfo;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MyPageController {

    private final MemberService memberService;
    private final BoardService boardService;

    /*=========================
     *		MY페이지 조회
     *=========================*/
    @GetMapping("/members/myPage")
    public String myPage(HttpSession session, Model model) {

        //로그인 회원 멤버 Dto 조회
        Member memberEntity = memberService.findMemberById(getLoginMemberId(session));
        ReadMemberForm member = new ReadMemberForm(memberEntity);
        log.info("회원 상세 정보 {}", member);

        model.addAttribute("member", member);

        return "template/myPage/myPage";
    }

    /*==================================
     * 	  마이페이지 - 내가 작성한 글
     *=================================*/
    @GetMapping(path={"/myPage/board"})
    public String myBoard(Model model, HttpSession session, @PageableDefault(page=1) Pageable pageable) {

        //내가 쓴 글 조회
        Page<ListBoardForm> boardListByLoginMember = boardService.getBoardListByLoginMember(getLoginMemberId(session), pageable);

        //템플릿에 보낼 페이징 정보
        templatePagingInfo(model, pageable, boardListByLoginMember, 10);


        return "template/myPage/board";
    }

}
