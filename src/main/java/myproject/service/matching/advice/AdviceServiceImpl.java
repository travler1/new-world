package myproject.service.matching.advice;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.web.matching.advice.dto.AdviceSearchCondition;
import myproject.domain.matching.advice.entity.Advice;
import myproject.domain.matching.advice.repository.AdviceRepository;
import myproject.domain.member.Member;
import myproject.service.member.MemberService;
import myproject.web.file.FileCategory;
import myproject.web.file.FileStore;
import myproject.web.file.UploadFile;
import myproject.web.matching.advice.dto.ListAdviceForm;
import myproject.web.matching.advice.dto.ReadAdviceForm;
import myproject.web.matching.advice.dto.SendAdviceForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class AdviceServiceImpl implements AdviceService{

    private final AdviceRepository adviceRepository;
    private final MemberService memberService;
    private final FileStore fileStore;

    //첨삭 저장
    public void saveAdviceForm(SendAdviceForm form, String senderIP, Long adviceId) throws IOException {
        Member sender = memberService.findMemberById(form.getSender());
        Member receiver = memberService.findMemberById(form.getReceiver());

        MultipartFile multipartFile = form.getUpload();
        UploadFile uploadFile = fileStore.storeFile(multipartFile, FileCategory.ADVICE);

        Advice advice = Advice.builder()
                .receiver(receiver)
                .sender(sender)
                .advice_content(form.getAdvice_content())
                .date_sent(new Date())
                .advice_ip(senderIP)
                .uploadFile(uploadFile)
                .build();

        adviceRepository.save(advice);

        //첨삭 답장 시 첨삭요청,첨삭답장 메세지에 대하여 첨삭완료처리.
        if (adviceId != null) {
            Advice adviceById = adviceRepository.findAdviceById(adviceId);
            if (adviceById.getAdvice_complete()==null) {
                adviceRepository.updateAdviceComplete(adviceId);
            }
            adviceRepository.updateAdviceComplete(advice.getId());
        }
    }

    //내가 받은 첨삭 리스트 페이징 + 검색
    @Override
    public Page<ListAdviceForm> getListReceivedAdvice(AdviceSearchCondition condition, Pageable pageable, Long memberId) {

        int page = pageable.getPageNumber()-1;
        int pageLimit = 5;
        Page<ListAdviceForm> listReceivedAdviceById = adviceRepository.findListReceivedAdviceById(condition, PageRequest.of(page, pageLimit), memberId);

        return listReceivedAdviceById;
    }

    //내가 보낸 첨삭 리스트 페이징 + 검색
    @Override
    public Page<ListAdviceForm> getListSentAdvice(AdviceSearchCondition condition, Pageable pageable, Long memberId) {

        int page = pageable.getPageNumber()-1;
        int pageLimit = 5;
        Page<ListAdviceForm> listSentAdviceById = adviceRepository.findListSentAdviceById(condition, PageRequest.of(page, pageLimit), memberId);
        return listSentAdviceById;
    }

    //첨삭 조회 및 읽음처리
    @Override
    public ReadAdviceForm getAdvice(Long id) {

        //첨삭 조회
        Advice adviceById = adviceRepository.findAdviceById(id);

        if (adviceById.getDate_read() == null) {
            //읽음처리
            adviceRepository.updateRead(id, new Date());
            log.info("첨삭 읽음처리 시간 ={}", new Date());
        }
        ReadAdviceForm readAdviceFormById = adviceRepository.findReadAdviceFormById(id);
        return readAdviceFormById;
    }

    //첨삭 답장 폼 호출
    @Override
    public SendAdviceForm respondAdviceById(Long adviceId) {

        Advice advice = adviceRepository.findAdviceById(adviceId);
        SendAdviceForm sendAdviceForm = new SendAdviceForm(advice.getSender().getId(), advice.getReceiver().getId());

        return sendAdviceForm;
    }

    //첨삭 단건 조회
    @Override
    public Advice getAdviceById(Long id) {
        return adviceRepository.findAdviceById(id);
    }

    //첨삭에 첨부된 파일 다운로드 시 해당 첨삭과 관련한 사용자인지 인증 체크
    @Override
    public Boolean getAuthCheck(Long adviceId, Long memberId) {

        Advice adviceById = adviceRepository.findAdviceById(adviceId);
        Boolean authCheck = false;
        if(adviceById.getSender().getId().equals(memberId)  || adviceById.getReceiver().getId().equals(memberId)){
            authCheck = true;
        }

        return authCheck;
    }
}
