package myproject.web.matching.advice;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import myproject.domain.member.Member;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class SendAdviceForm {

    private Long sender;
    private Long receiver;
    @NotBlank
    private String advice_content;
    private MultipartFile upload;

}
