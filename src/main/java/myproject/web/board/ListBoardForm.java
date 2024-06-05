package myproject.web.board;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.util.Date;

@Data
public class ListBoardForm {

    private Long id;
    private String title;
    private String username;
    private Date regDate;
    private Long boardHit;
    private Long boardFav;
    private Long boardReply;

    @QueryProjection
    public ListBoardForm(Long id, String title, String username, Date regDate, Long boardHit, Long boardFav, Long boardReply) {
        this.id = id;
        this.title = title;
        this.username = username;
        this.regDate = regDate;
        this.boardHit = boardHit;
        this.boardFav = boardFav;
        this.boardReply = boardReply;
    }
}
