package myproject.web.board.dto.boardDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class SaveBoardForm {

    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private MultipartFile upload;
}
