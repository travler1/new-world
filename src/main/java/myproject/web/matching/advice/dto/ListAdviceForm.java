package myproject.web.matching.advice.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.util.Date;

@Data
public class ListAdviceForm {

    private Long id;
    private String username;

    private Date date_sent;
    private Date date_read;

    private Boolean advice_complete;

    @QueryProjection
    public ListAdviceForm(Long id, String username, Date date_sent, Date date_read, Boolean advice_complete) {
        this.id = id;
        this.username=username;
        this.date_sent = date_sent;
        this.date_read = date_read;
        this.advice_complete = advice_complete;
    }
}
