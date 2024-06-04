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
