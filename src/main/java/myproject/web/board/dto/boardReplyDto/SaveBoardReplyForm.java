package myproject.web.board.dto.boardReplyDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SaveBoardReplyForm {

    private Long boardId;
    @NotBlank
    private String content;
}
