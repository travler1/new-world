package myproject.web.board;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import myproject.domain.board.BoardReply;
import myproject.domain.member.EmbeddedDate;
import myproject.web.file.UploadFile;

import java.util.List;

@Data
public class ReadBoardForm {

    private Long id;
    private String title;
    private String content;
    private Long hit;
    private EmbeddedDate date;
    private UploadFile uploadFile;
    private String ip;
    private Long memberId;
    private String username;
    private Long boardFavCount;
    private List<BoardReply> boardReplyList;

    @QueryProjection
    public ReadBoardForm(Long id, String title, String content, Long hit, Long memberId,String username, EmbeddedDate date, UploadFile uploadFile, String ip, Long boardFavCount) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.hit = hit;
        this.memberId=memberId;
        this.username=username;
        this.date = date;
        this.uploadFile = uploadFile;
        this.ip = ip;
        this.boardFavCount = boardFavCount;
    }

}
