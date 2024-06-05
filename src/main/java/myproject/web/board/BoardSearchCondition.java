package myproject.web.board;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoardSearchCondition {

    private Integer keyfield; // 1. 제목, 2. 닉네임, 3. 내용, 4. 제목 + 내용
    private String keyword;// 검색어
    private Integer order; // 1. 최신순, 2. 좋아요순, 3. 조회순, 4. 댓글순
}
