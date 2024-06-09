package myproject.web.matching.advice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendAdviceForm {

    private Long sender;
    private Long receiver;
    @NotBlank
    private String advice_content;
    private MultipartFile upload;

    public SendAdviceForm(Long sender, Long receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }
}
