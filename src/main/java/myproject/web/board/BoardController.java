package myproject.web.board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.domain.board.BoardService;
import myproject.domain.member.MemberService;
import myproject.web.member.MemberDTO.SessionMemberForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Transactional
@Slf4j
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final MemberService memberService;

    @GetMapping("")
    public String Board(Model model) {





        model.addAttribute("keyword", null);
        model.addAttribute("keyfield", null);
        model.addAttribute("count", null);
        model.addAttribute("order", null);
        model.addAttribute("start", null);
        model.addAttribute("end", null);

        model.addAttribute("page", null);

        List<BoardListDto> boardListDtos = boardService.boardListDto();
        model.addAttribute("list", boardListDtos);

        log.info("게시판 글 목록 출력 ={}", boardListDtos);

        return "template/board/main";
    }

    @GetMapping("/test")
    public String test(@RequestParam(required = false) Integer keyfield,
                       @RequestParam(required = false) String keyword,
                       @RequestParam(required = false) Integer order,
                       @PageableDefault(page=1) Pageable pageable, Model model) {




        BoardSearchCondition condition = new BoardSearchCondition(keyfield, keyword, order);
        Page<BoardListDto> boardListDtos11 = boardService.searchBoards(condition, pageable);

        /**
         * blockLimit : page 개수 설정
         * 현재 사용자가 선택한 페이지 앞 뒤로 3페이지씩만 보여준다.
         * ex : 현재 사용자가 4페이지라면 2, 3, (4), 5, 6
         */
        int blockLimit = 10;
        int startPage = (((int) Math.ceil(((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
        int endPage = Math.min((startPage + blockLimit - 1), boardListDtos11.getTotalPages());
        model.addAttribute("list", boardListDtos11);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "template/board/searchTest";
    }

    @GetMapping("/write")
    public String boardWrite(@ModelAttribute("saveBoardForm") SaveBoardForm saveBoardForm,
                             Model model) {


        return "template/board/write";
    }

    @PostMapping("/write")
    public String boardSave(@Valid  @ModelAttribute("saveBoardForm")SaveBoardForm saveBoardForm, BindingResult bindingResult,
                            HttpSession session, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) throws IOException {

        if (bindingResult.hasErrors()) {
            return "template/board/write";
        }

        SessionMemberForm sessionMemberForm = (SessionMemberForm)session.getAttribute("loginMember");
        String ip = request.getRemoteAddr();

        boardService.register(saveBoardForm, ip, sessionMemberForm.getId());

        //View에 표시할 메시지

        redirectAttributes.addFlashAttribute("message", "글쓰기가 완료되었습니다.");
        redirectAttributes.addFlashAttribute("url", request.getContextPath()+ "/board");

        return "redirect:/common/resultAlert";
    }

    /*==================================
     * 			게시판 글 상세
     *=================================*/
    @GetMapping("/{id}")
    public String boardDetail(@PathVariable("id") Long id, Model model) {

        ReadBoardForm readBoardForm = boardService.readBoardDetail(id);
        model.addAttribute("board", readBoardForm);
        log.info("게시판 글 상세 ={}", readBoardForm);

        return "template/board/boardDetail";
    }




}
