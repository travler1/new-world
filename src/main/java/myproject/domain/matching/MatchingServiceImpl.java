package myproject.domain.matching;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.domain.member.EmbeddedDate;
import myproject.domain.member.Member;
import myproject.domain.member.MemberService;
import myproject.web.file.FileCategory;
import myproject.web.file.FileStore;
import myproject.web.file.UploadFile;
import myproject.web.matching.EmpEditForm;
import myproject.web.matching.EmpRegisterForm;
import myproject.web.member.MemberDTO.SessionMemberForm;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MatchingServiceImpl implements MatchingService {

    private final MatchingRepository matchingRepository;
    private final MemberService memberService;
    private final FileStore fileStore;

    @Override
    public void registerEmp(EmpRegisterForm empRegisterForm, SessionMemberForm sessionMemberForm) throws IOException {
        UploadFile uploadFile = null;
        try{

            //로그인한 회원 조회

            Member member = memberService.findMemberById(sessionMemberForm.getId());

            //EmpInfo의 파일 저장
            uploadFile = fileStore.storeFile(empRegisterForm.getMultipartFile(), FileCategory.EMP_INFO);
            //EmpRegisterForm -> EmpInfo Entity 변경
            EmpInfo empInfoBuild = EmpInfo.builder()
                    .comSize(empRegisterForm.getComSize())
                    .comPeople(empRegisterForm.getComPeople())
                    .field(empRegisterForm.getField())
                    .role(empRegisterForm.getRole())
                    .career(empRegisterForm.getCareer())
                    .salary_status(empRegisterForm.getSalary_status())
                    .salary(empRegisterForm.getSalary())
                    .periodTime(empRegisterForm.getPeriodTime())
                    .education(empRegisterForm.getEducation())
                    .major(empRegisterForm.getMajor())
                    .certification(empRegisterForm.getCertification())
                    .location(empRegisterForm.getLocation())
                    .location_api(empRegisterForm.getLocation_api())
                    .location_api_lat(empRegisterForm.getLocation_api_lat())
                    .location_api_lng(empRegisterForm.getLocation_api_lng())
                    .workStart(empRegisterForm.getWorkStart())
                    .uploadFile(uploadFile)
                    .date(new EmbeddedDate(new Date(), null))
                    .advice(empRegisterForm.getAdvice())
                    .member(member)
                    .build();

            //empInfo 데이터 저장
            matchingRepository.save(empInfoBuild);
        }catch(Exception e){
            log.info("EmpInfo 파일 저장 실패");
            if(uploadFile != null){
                fileStore.deleteFile(uploadFile, FileCategory.EMP_INFO);
            }
            throw e;
        }
    }

    @Override
    public EmpEditForm findMyEmp(SessionMemberForm sessionMemberForm) {

        //로그인 한 회원 조회
        Member member = memberService.findMemberById(sessionMemberForm.getId());
        //로그인한 회원의 취업 등록 정보 조회
        EmpInfo empInfo = matchingRepository.findEmpInfo(member);
        //취업 등록 정보 empInfo DTO로 변환
        EmpEditForm empEditForm = EmpEditForm.builder()
                .comSize(empInfo.getComSize())
                .comPeople(empInfo.getComPeople())
                .field(empInfo.getField())
                .role(empInfo.getRole())
                .career(empInfo.getCareer())
                .salary_status(empInfo.getSalary_status())
                .salary(empInfo.getSalary())
                .periodTime(empInfo.getPeriodTime())
                .education(empInfo.getEducation())
                .major(empInfo.getMajor())
                .certification(empInfo.getCertification())
                .location(empInfo.getLocation())
                .location_api(empInfo.getLocation_api())
                .location_api_lat(empInfo.getLocation_api_lat())
                .location_api_lng(empInfo.getLocation_api_lng())
                .workStart(empInfo.getWorkStart())
                .uploadFile(empInfo.getUploadFile())
                .date(empInfo.getDate())
                .advice(empInfo.getAdvice())
                .build();

        return empEditForm;
    }
}
