package myproject.domain.matching.advice.repository;

import myproject.web.matching.advice.dto.AdviceSearchCondition;
import myproject.web.matching.advice.dto.ListAdviceForm;
import myproject.web.matching.advice.dto.ReadAdviceForm;
import org.springframework.data.domain.Page;
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
