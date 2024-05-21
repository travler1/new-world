package myproject.domain.member;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public enum Grade {
    STUDENT, WORKER, ADMIN
}
