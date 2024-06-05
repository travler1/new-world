package myproject.domain.matching.advice;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdviceSearchCondition {

    private Integer keyfield;
    private String keyword;
}
