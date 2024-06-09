package myproject.service.matching.advice;

import myproject.web.matching.advice.dto.AdviceSearchCondition;
import myproject.domain.matching.advice.entity.Advice;
import myproject.web.matching.advice.dto.ListAdviceForm;
import myproject.web.matching.advice.dto.ReadAdviceForm;
import myproject.web.matching.advice.dto.SendAdviceForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface AdviceService {

    //첨삭 저장
    void saveAdviceForm(SendAdviceForm form,String senderIP, Long adviceId) throws IOException;

    Page<ListAdviceForm> getListReceivedAdvice(AdviceSearchCondition condition, Pageable pageable, Long memberId);

    ReadAdviceForm getAdvice(Long id);

    Advice getAdviceById(Long id);

    SendAdviceForm respondAdvice(Long adviceId, Long loginMember);

    Page<ListAdviceForm> getListSentAdvice(AdviceSearchCondition condition, Pageable pageable, Long loginMemberId);

    //첨삭에 첨부된 파일 다운로드 시 해당 첨삭과 관련한 사용자인지 인증 체크
    Boolean getAuthCheck(Long adviceId, Long memberId);
}
