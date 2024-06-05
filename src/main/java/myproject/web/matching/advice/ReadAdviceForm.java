package myproject.web.matching.advice;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import myproject.domain.member.Member;
import myproject.web.file.UploadFile;

import java.util.Date;

@Data
public class ReadAdviceForm {

    private Long id;
    private Member receiver;
    private Member sender;
    private String advice_content;

    private Date date_sent;
    private Date date_read;

    private Boolean advice_complete;

    private UploadFile uploadFile;

    @QueryProjection
    public ReadAdviceForm(Long id, Member receiver, Member sender, String advice_content, Date date_sent, Date date_read, Boolean advice_complete, UploadFile uploadFile) {
        this.id = id;
        this.receiver = receiver;
        this.sender = sender;
        this.advice_content = advice_content;
        this.date_sent = date_sent;
        this.date_read = date_read;
        this.advice_complete = advice_complete;
        this.uploadFile = uploadFile;
    }
}
