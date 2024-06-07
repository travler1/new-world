package myproject.domain.board.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import myproject.domain.member.EmbeddedDate;
import myproject.domain.member.Member;
import myproject.web.file.UploadFile;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Board {

    @Id @GeneratedValue
    private Long id;

    private String title;

    @Lob
    private String content;

    private Long hit;

    @Embedded
    private EmbeddedDate date;
    @Embedded
    private UploadFile uploadFile;

    private String ip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardFav> boardFavList = new ArrayList<BoardFav>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardReply> boardReplyList = new ArrayList<>();

    @Builder
    public Board(String title, String content, Long hit, EmbeddedDate date, UploadFile uploadFile, String ip, Member member) {
        this.title = title;
        this.content = content;
        this.hit = hit;
        this.date = date;
        this.uploadFile = uploadFile;
        this.ip = ip;
        this.member = member;
    }
}
