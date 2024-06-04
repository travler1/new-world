package myproject.domain.board;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.domain.board.boardFav.BoardFavRepository;
import myproject.domain.member.EmbeddedDate;
import myproject.domain.member.Member;
import myproject.domain.member.MemberService;
import myproject.web.board.BoardListDto;
import myproject.web.board.ReadBoardForm;
import myproject.web.board.SaveBoardForm;
import myproject.web.file.FileCategory;
import myproject.web.file.FileStore;
import myproject.web.file.UploadFile;
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

    //게시판 조회

    @Override
    public Board getBoard(Long boardId) {

        Board boardById = boardRepository.findBoardById(boardId);

        return boardById;
    }
}
