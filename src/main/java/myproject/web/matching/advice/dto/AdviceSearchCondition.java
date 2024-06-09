package myproject.web.matching.advice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdviceSearchCondition {

    private Integer keyfield;
    private String keyword;
}
