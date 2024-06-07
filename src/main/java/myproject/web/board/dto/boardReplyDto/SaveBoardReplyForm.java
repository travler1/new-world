package myproject.web.board.dto.boardReplyDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import myproject.domain.board.BoardReply;

@Data
public class SaveBoardReplyForm {

    private Long boardId;
    @NotBlank
    private String content;

    public BoardReply toEntity(SaveBoardReplyForm form) {
        return new BoardReply().builder()
                .content(content)
                .build();
    }
}
