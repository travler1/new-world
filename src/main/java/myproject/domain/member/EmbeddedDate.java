package myproject.domain.member;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;

import java.util.Date;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Data
public class EmbeddedDate {

    @Temporal(TemporalType.TIMESTAMP)
    private Date reg_date;
    @Temporal(TemporalType.TIMESTAMP)
    private Date modify_date;
}
