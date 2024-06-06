package myproject.web.board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.domain.board.BoardService;
import myproject.service.member.MemberService;
import myproject.web.file.FileStore;
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

@Controller
@RequiredArgsConstructor
@Transactional
@Slf4j
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final MemberService memberService;
    private final FileStore fileStore;

    //세션에 로그인된 회원의 아이디 조회 메서드
    private Long getLoginMemberId(HttpSession session) {
        SessionMemberForm loginMember = (SessionMemberForm) session.getAttribute("loginMember");
        if (loginMember == null) {
            throw new IllegalStateException("로그인 정보가 없습니다.");
        }
        return loginMember.getId();
    }

    /*==================================
     * 		  게시판 메인 페이지
     *=================================*/
    @GetMapping("")
    public String test(@RequestParam(required = false) Integer keyfield,
                       @RequestParam(required = false) String keyword,
                       @RequestParam(required = false) Integer order,
                       @PageableDefault(page = 1) Pageable pageable, Model model) {


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

        log.info("게시판 글 조회 ={}", boardListDtos11.getTotalElements());

        return "template/board/main";
    }

    /*==================================
     * 		   게시판 글 등록 폼 호출
     *=================================*/
    @GetMapping("/write")
    public String boardWrite(@ModelAttribute("saveBoardForm") SaveBoardForm saveBoardForm,
                             Model model) {


        return "template/board/write";
    }

    /*==================================
     * 			게시판 글 등록
     *=================================*/
    @PostMapping("/write")
    public String boardSave(@Valid @ModelAttribute("saveBoardForm") SaveBoardForm saveBoardForm, BindingResult bindingResult,
                            HttpSession session, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) throws IOException {

        if (bindingResult.hasErrors()) {
            return "template/board/write";
        }

        SessionMemberForm sessionMemberForm = (SessionMemberForm) session.getAttribute("loginMember");
        String ip = request.getRemoteAddr();

        boardService.register(saveBoardForm, ip, sessionMemberForm.getId());

        //View에 표시할 메시지

        redirectAttributes.addFlashAttribute("message", "글쓰기가 완료되었습니다.");
        redirectAttributes.addFlashAttribute("url", request.getContextPath() + "/board");

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

    /*==================================
     * 		  게시판 글 수정 폼 호출
     *=================================*/
    @GetMapping("/{id}/edit")
    public String boardEdit(@PathVariable("id") Long id, Model model) {

        EditBoardForm editBoardForm = boardService.getEditBoardFormById(id);
        model.addAttribute("editBoardForm", editBoardForm);

        return "template/board/edit";
    }

    /*==================================
     * 		   게시판 글 수정
     *=================================*/
    @PostMapping("/{id}/edit")
    public String boardEditSave(@PathVariable("id") Long id, Model model, HttpSession session,
                                @Valid @ModelAttribute("editBoardForm") EditBoardForm editBoardForm,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                HttpServletRequest request) throws IOException {

        if (bindingResult.hasErrors()) {
            log.info("글수정 에러 발생, editBoardForm={}", editBoardForm);
            return "template/board/edit";
        }


        String ip = request.getRemoteAddr();

        boardService.edit(editBoardForm, ip, getLoginMemberId(session));

        redirectAttributes.addFlashAttribute("message", "글수정이 완료되었습니다.");
        redirectAttributes.addFlashAttribute("url", request.getContextPath() + "/board");

        log.info("글 수정 메서드 마지막 진입");
        return "redirect:/common/resultAlert";
    }

    /*==================================
     * 		   게시판 글 삭제
     *=================================*/
    @GetMapping("/{id}/delete")
    public String deleteBoard(@PathVariable("id") Long id, Model model) {

        //board의 파일 삭제 후 해당 board 삭제
        boardService.deleteBoard(id);

        return "redirect:/board";
    }

}
