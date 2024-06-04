package myproject.web.board;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;

@Data
public class UpdateReplyForm {

    private Long boardReplyId;
    @NotBlank
    private String boardReplyContent;
    private String ip;
    private Date modify_date;

}
