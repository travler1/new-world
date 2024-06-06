package myproject.domain.board;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.domain.board.boardFav.BoardFavRepository;
import myproject.domain.member.EmbeddedDate;
import myproject.domain.member.Member;
import myproject.service.member.MemberService;
import myproject.web.board.*;
import myproject.web.file.FileCategory;
import myproject.web.file.FileStore;
import myproject.web.file.UploadFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final BoardFavRepository boardFavRepository;
    private final MemberService memberService;
    private final FileStore fileStore;

    //게시판 글 저장
    @Override
    public void register(SaveBoardForm form, String ip, Long loginMemberId) throws IOException {

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

        boardRepository.save(board);
    }

    @Override
    public List<BoardListDto> boardListDto() {
        return boardRepository.boardListDtos();
    }

    //게시판 글 조회(+조회수 증가)
    @Override
    public ReadBoardForm readBoardDetail(Long id) {
        boardRepository.updateboardHit(id);
        ReadBoardForm findBoard = boardRepository.findReadBoardFormById(id);

        return findBoard;
    }

    //게시판 좋아요 유무 확인
    @Override
    public Optional<BoardFav> getFav(Long boardId, Long memberId) {
        Optional<BoardFav> findFav = boardFavRepository.findFav(boardId, memberId);
        return findFav;
    }

    //게시판 좋아요 갯수 출력
    @Override
    public Long getFavCount(Long boardId) {
        Long favCount = boardFavRepository.findFavCount(boardId);
        return favCount;
    }

    //좋아요 삭제
    @Override
    public void deleteBoardFav(Long boardFavId) {
        boardFavRepository.deleteBoardFavById(boardFavId);
    }

    //좋아요 등록
    @Override
    public void registerFav(Long boardId, Long memberId) {
        Board board = boardRepository.findBoardById(boardId);
        Member member = memberService.findMemberById(memberId);
        BoardFav boardFav = new BoardFav().builder()
                .board(board)
                .member(member)
                .build();
        boardFavRepository.save(boardFav);
    }

    //게시판 단건 조회
    @Override
    public Board getBoard(Long boardId) {

        Board boardById = boardRepository.findBoardById(boardId);

        return boardById;
    }

    //게시판 페이징
    @Override
    public Page<BoardListDto> searchBoards(BoardSearchCondition condition, Pageable pageable) {

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
    public void edit(EditBoardForm form, String ip, Long loginMemberId) throws IOException {

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

        boardRepository.update(form.getId(), editBoard);
    }

    //게시글 삭제
    @Override
    public void deleteBoard(Long id) {

        Board board = getBoard(id);
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
