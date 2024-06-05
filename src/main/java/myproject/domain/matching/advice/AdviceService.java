package myproject.domain.matching.advice;

import jakarta.servlet.http.HttpServletRequest;
import myproject.web.matching.advice.ListAdviceForm;
import myproject.web.matching.advice.ReadAdviceForm;
import myproject.web.matching.advice.SendAdviceForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface AdviceService {

    //첨삭 저장
    void saveAdviceForm(SendAdviceForm form,String senderIP, Long adviceId) throws IOException;

    Page<ListAdviceForm> getListReceivedAdvice(AdviceSearchCondition condition, Pageable pageable, Long memberId);

    ReadAdviceForm getAdvice(Long id);

    SendAdviceForm respondAdvice(Long adviceId, Long loginMember);

    Page<ListAdviceForm> getListSentAdvice(AdviceSearchCondition condition, Pageable pageable, Long loginMemberId);
}
