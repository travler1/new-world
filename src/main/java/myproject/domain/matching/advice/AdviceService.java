package myproject.domain.matching.advice;

import jakarta.servlet.http.HttpServletRequest;
import myproject.web.matching.advice.SendAdviceForm;

import java.io.IOException;

public interface AdviceService {

    //첨삭 저장
    void saveAdviceForm(SendAdviceForm form,String senderIP) throws IOException;
}
