package myproject.domain.matching.advice.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import myproject.domain.member.Member;
import myproject.web.file.UploadFile;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class Advice {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "advice_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION) //사용자가 계정 삭제시 같이 삭제
    private Member receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION) //사용자가 계정 삭제시 같이 삭제
    private Member sender;

    @Lob
    private String advice_content;

    private Date date_sent;
    private Date date_read;


    private Boolean receive_read; //true : read, false : unread

    private Boolean receive_del; //true : delete, false : undelete;

    private Boolean sent_del; //true : delete, false : undelete;

    private Boolean advice_complete; //true : answer complete, false : answer uncomplete

    private String advice_ip;

    @Embedded
    private UploadFile uploadFile;

    public void deleteByReceiver(){
        this.receive_del = true;
    }

    public void deleteBySender() {
        this.sent_del = true;
    }

    public boolean isDelete() {
        return this.receive_del && this.sent_del;
    }

    public Date getDate_read() {
        return date_read;
    }

    @Builder
    public Advice(Member receiver, Member sender, String advice_content, Date date_sent, Date date_read, Boolean receive_read, Boolean receive_del, Boolean sent_del, Boolean advice_complete, String advice_ip, UploadFile uploadFile) {
        this.receiver = receiver;
        this.sender = sender;
        this.advice_content = advice_content;
        this.date_sent = date_sent;
        this.date_read = date_read;
        this.receive_read = receive_read;
        this.receive_del = receive_del;
        this.sent_del = sent_del;
        this.advice_complete = advice_complete;
        this.advice_ip = advice_ip;
        this.uploadFile = uploadFile;
    }
}
