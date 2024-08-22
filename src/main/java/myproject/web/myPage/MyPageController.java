package myproject.web.myPage;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.LoginAccount;
import myproject.service.board.BoardService;
import myproject.domain.member.Member;
import myproject.web.board.dto.boardDto.ListBoardForm;
import myproject.web.member.MemberDTO.ReadMemberForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static myproject.Util.templatePagingInfo;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MyPageController {

    private final BoardService boardService;

    /*=========================
     *		MY페이지 조회
     *=========================*/
    @GetMapping("/members/myPage")
    public String myPage(@LoginAccount Member member, Model model) {

        //회원정보 Dto 조회
        ReadMemberForm memberForm = new ReadMemberForm(member);
        log.info("회원 상세 정보 {}", member);

        model.addAttribute("member", memberForm);

        return "template/myPage/myPage";
    }

    /*==================================
     * 	  마이페이지 - 내가 작성한 글
     *=================================*/
    @GetMapping("/myPage/board")
    public String myBoard(Model model, @LoginAccount Member member, @PageableDefault(page=1) Pageable pageable) {

        //내가 쓴 글 조회
        Page<ListBoardForm> boardListByLoginMember = boardService.getBoardListByLoginMember(member.getId(), pageable);
        //템플릿에 보낼 페이징 정보
        templatePagingInfo(model, pageable, boardListByLoginMember, 10);

        return "template/myPage/board";
    }

}
