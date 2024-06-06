package myproject.domain.image;

import jakarta.persistence.*;
import lombok.*;
import myproject.domain.member.Member;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "image")
public class Image {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String url;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void updateURL(String url) {
        this.url = url;
    }
}
