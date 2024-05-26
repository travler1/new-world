package myproject.domain.member;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.*;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class Address {

    private String zipcode;
    private String address1;
    private String address2;
}
