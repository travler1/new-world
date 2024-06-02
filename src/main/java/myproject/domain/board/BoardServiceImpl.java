package myproject.domain.board;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.domain.member.EmbeddedDate;
import myproject.domain.member.Member;
import myproject.domain.member.MemberService;
import myproject.web.board.BoardListDto;
import myproject.web.board.SaveBoardForm;
import myproject.web.file.FileCategory;
import myproject.web.file.FileStore;
import myproject.web.file.UploadFile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
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
    public Board readBoardDetail(Long id) {
        boardRepository.updateboardHit(id);
        Board findBoard = boardRepository.findBoardById(id);

        //Board Entity -> BoardReadForm 으로 변환

        return findBoard;
    }
}
