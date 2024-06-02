package myproject.domain.board;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import myproject.domain.member.EmbeddedDate;
import myproject.domain.member.Member;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardReply {

    @Id @GeneratedValue
    private Long id;

    @Lob
    private String content;

    @Embedded
    private EmbeddedDate date;

    private String ip;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public BoardReply(String content, EmbeddedDate date, String ip, Board board, Member member) {
        this.content = content;
        this.date = date;
        this.ip = ip;
        this.board = board;
        this.member = member;
    }
}
