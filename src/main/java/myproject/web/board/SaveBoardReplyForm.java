package myproject.web.board;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SaveBoardReplyForm {

    private Long boardId;
    @NotBlank
    private String content;
}
