package myproject.domain.matching.advice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.domain.member.Member;
import myproject.domain.member.MemberRepository;
import myproject.domain.member.MemberService;
import myproject.web.file.FileCategory;
import myproject.web.file.FileStore;
import myproject.web.file.UploadFile;
import myproject.web.matching.advice.SendAdviceForm;
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
    public void saveAdviceForm(SendAdviceForm form, String senderIP) throws IOException {
        Member sender = memberService.findMemberById(form.getSender());
        Member receiver = memberService.findMemberById(form.getReceiver());

        MultipartFile multipartFile = form.getUpload();
        UploadFile uploadFile = fileStore.storeFile(multipartFile, FileCategory.ADVICE);

        Advice advice = new Advice.AdviceBuilder()
                .receiver(receiver)
                .sender(sender)
                .advice_content(form.getAdvice_content())
                .date_sent(new Date())
                .advice_ip(senderIP)
                .uploadFile(uploadFile).build();

        adviceRepository.save(advice);

    }
}
