package myproject.domain.matching.advice;

import myproject.web.matching.advice.ListAdviceForm;
import myproject.web.matching.advice.ReadAdviceForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface AdviceRepositoryCustom {

    Page<ListAdviceForm> findListReceivedAdviceById(AdviceSearchCondition condition, Pageable pageable, Long memberId);

    //첨삭 읽음처리
    void updateRead(Long id, Date date);

    ReadAdviceForm findReadAdviceFormById(Long id);

    void updateAdviceComplete(Long adviceId);

    Page<ListAdviceForm> findListSentAdviceById(AdviceSearchCondition condition, Pageable pageable, Long memberId);
}
