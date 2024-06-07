package myproject.web.board.dto.boardDto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import myproject.web.file.UploadFile;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class EditBoardForm {

    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String content;

    private UploadFile uploadFile;
    private MultipartFile upload;
}
