package myproject.web.board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.LoginAccount;
import myproject.service.board.BoardService;
import myproject.domain.member.Member;
import myproject.service.member.MemberService;
import myproject.web.board.dto.boardDto.*;
import myproject.web.file.FileStore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

import static myproject.Util.*;

@Controller
@RequiredArgsConstructor
@Transactional
@Slf4j
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final MemberService memberService;
    private final FileStore fileStore;

    /*==================================
     * 		  게시판 메인 페이지
     *=================================*/
    @GetMapping("")
    public String test(@RequestParam(required = false, defaultValue = "1") Integer keyfield,
                       @RequestParam(required = false) String keyword,
                       @RequestParam(required = false, defaultValue = "1") Integer order,
                       @PageableDefault(page = 1) Pageable pageable, Model model) {


        BoardSearchCondition condition = new BoardSearchCondition(keyfield, keyword, order);
        Page<ListBoardForm> listBoardForms = boardService.searchBoardList(condition, pageable);


        templatePagingInfo(model, pageable, listBoardForms, 10);

        log.info("게시판 글 조회 ={}", listBoardForms.getTotalElements());

        return "template/board/main";
    }

    /*==================================
     * 		   게시판 글 등록 폼 호출
     *=================================*/
    @GetMapping("/write")
    public String boardWrite(@ModelAttribute("saveBoardForm") SaveBoardForm saveBoardForm,
                             @LoginAccount Member member, RedirectAttributes redirectAttributes,
                             HttpServletRequest request, Model model) {

        if(member == null) {
            commonResultAlert("로그인한 회원만 이용 가능합니다.", "/loginForm", redirectAttributes, request);
            return "redirect:/common/resultAlert";
        }

        return "template/board/write";
    }

    /*==================================
     * 			게시판 글 등록
     *=================================*/
    @PostMapping("/write")
    public String boardSave(@Valid @ModelAttribute("saveBoardForm") SaveBoardForm saveBoardForm,
                            BindingResult bindingResult,
                            @LoginAccount Member member,
                            HttpServletRequest request,
                            RedirectAttributes redirectAttributes) throws IOException {

        if (bindingResult.hasErrors()) {
            return "template/board/write";
        }

        String ip = request.getRemoteAddr();
        Long saveBoardId = boardService.register(saveBoardForm, ip, member.getId());

        //View에 표시할 메시지
        commonResultView("Hello World - 글쓰기", "글쓰기가 완료되었습니다.", request.getContextPath() + "/board/" + saveBoardId, redirectAttributes, request);

        return "redirect:/common/resultView";
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
                                @LoginAccount Member member,
                                RedirectAttributes redirectAttributes,
                                HttpServletRequest request) throws IOException {

        if (bindingResult.hasErrors()) {
            log.info("글수정 에러 발생, editBoardForm={}", editBoardForm);
            return "template/board/edit";
        }


        String ip = request.getRemoteAddr();

        log.info("글 수정 폼 정보={}", editBoardForm);
        Long updateBoardId = boardService.edit(editBoardForm, ip, member.getId());
        log.info("글 수정 폼 id={}",updateBoardId);

        commonResultView("Hello World 글 수정", "글 수정이 완료되었습니다.", request.getContextPath() + "/board/" + updateBoardId, redirectAttributes, request);

        log.info("글 수정 완료, editBoardForm={}", editBoardForm);
        return "redirect:/common/resultView";
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
