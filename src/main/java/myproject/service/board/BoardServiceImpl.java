package myproject.service.board;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.domain.board.entity.Board;
import myproject.domain.board.repository.BoardRepository;
import myproject.domain.member.EmbeddedDate;
import myproject.domain.member.Member;
import myproject.service.member.MemberService;
import myproject.web.board.dto.boardDto.*;
import myproject.web.file.FileCategory;
import myproject.web.file.FileStore;
import myproject.web.file.UploadFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final MemberService memberService;
    private final FileStore fileStore;

    //게시판 글 저장
    @Override
    public Long register(SaveBoardForm form, String ip, Long loginMemberId) throws IOException {

        UploadFile uploadFile = fileStore.storeFile(form.getUpload(), FileCategory.BOARD);
        Member loginMember = memberService.findMemberById(loginMemberId);

        Board board = new Board().builder()
                .title(form.getTitle())
                .content(form.getContent())
                .hit(0L)
                .date(new EmbeddedDate(new Date(), null))
                .uploadFile(uploadFile)
                .ip(ip)
                .member(loginMember)
                .build();

        Board saveBoard =  boardRepository.save(board);
        return saveBoard.getId();
    }

    //게시판 글 조회(+조회수 증가)
    @Override
    public ReadBoardForm readBoardDetail(Long id) {
        boardRepository.updateboardHit(id);
        ReadBoardForm findBoard = boardRepository.findReadBoardFormById(id);

        return findBoard;
    }



    //게시판 단건 조회
    @Override
    public Board getBoardById(Long boardId) {

        Board boardById = boardRepository.findBoardById(boardId);

        return boardById;
    }

    //게시판 페이징
    @Override
    public Page<ListBoardForm> searchBoardList(BoardSearchCondition condition, Pageable pageable) {

        int page = pageable.getPageNumber()-1; //page 위치에 있는 값은 0부터 시작한다.
        int pageLimit = 10; //한 페이지에 보여줄 글 개수

        //한 페이지당 10개 씩 글을 보여줌
        return boardRepository.search(condition, PageRequest.of(page, pageLimit));
    }

    //게시판 수정 폼 호출
    @Override
    public EditBoardForm getEditBoardFormById(Long boardId) {

        Board findBoard = boardRepository.findBoardById(boardId);
        EditBoardForm editBoardForm = new EditBoardForm(
                findBoard.getId(),
                findBoard.getTitle(),
                findBoard.getContent(),
                findBoard.getUploadFile(),
                null
        );

        return editBoardForm;
    }

    //게시판 글 수정
    @Override
    public Long edit(EditBoardForm form, String ip, Long loginMemberId) throws IOException {

        if (form.getUploadFile() != null) {
            fileStore.deleteFile(form.getUploadFile(), FileCategory.BOARD);
        }
        UploadFile uploadFile = fileStore.storeFile(form.getUpload(), FileCategory.BOARD);
        Board originBoard = boardRepository.findBoardById(form.getId());
        EmbeddedDate date = originBoard.getDate();
        date.setModify_date(new Date());

        Board editBoard = new Board().builder()
                .title(form.getTitle())
                .content(form.getContent())
                .uploadFile(uploadFile)
                .ip(ip)
                .date(date)
                .build();

        Long updateBoardId = boardRepository.update(form.getId(), editBoard);
        log.info("updateBoardId={}", updateBoardId);
        return updateBoardId;
    }

    //게시글 삭제
    @Override
    public void deleteBoard(Long id) {

        Board board = getBoardById(id);
        if (board.getUploadFile() != null) {
            fileStore.deleteFile(board.getUploadFile(), FileCategory.BOARD);
        }
        boardRepository.delete(board);
    }

    //내가 작성한 글 리스트 조회
    @Override
    public Page<ListBoardForm> getBoardListByLoginMember(Long loginMemberId, Pageable pageable) {

        int page = pageable.getPageNumber()-1; //page 위치에 있는 값은 0부터 시작한다.
        int pageLimit = 10; //한 페이지에 보여줄 글 개수

        Page<ListBoardForm> boardListByMemberId = boardRepository.findBoardListByMemberId(loginMemberId, PageRequest.of(page, pageLimit));

        return boardListByMemberId;
    }
}
