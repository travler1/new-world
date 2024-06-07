package myproject.web.board.dto.boardReplyDto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.util.Date;

@Data
public class ReadBoardReplyForm {

    private Long id;
    private String content;
    private Long boardId;
    private Long memberId;
    private String username;
    private Date reg_date;
    private Date modify_date;

    @QueryProjection
    public ReadBoardReplyForm(Long id, String content, Long boardId, Long memberId, String username, Date reg_date, Date modify_date) {
        this.id = id;
        this.content = content;
        this.boardId = boardId;
        this.memberId = memberId;
        this.username = username;
        this.reg_date = reg_date;
        this.modify_date = modify_date;
    }

}
