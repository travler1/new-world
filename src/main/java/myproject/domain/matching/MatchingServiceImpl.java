package myproject.domain.matching;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.domain.member.EmbeddedDate;
import myproject.domain.member.Member;
import myproject.service.member.MemberService;
import myproject.web.file.FileCategory;
import myproject.web.file.FileStore;
import myproject.web.file.UploadFile;
import myproject.web.matching.EmpEditForm;
import myproject.web.matching.EmpRegisterForm;
import myproject.web.member.MemberDTO.SessionMemberForm;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

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
                .id(empInfo.getId())
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
                .member(empInfo.getMember())
                .build();

        return empEditForm;
    }

    @Override //다른 회원의 취업정보 조회
    public EmpEditForm findEmpByMemberId(Long id) {

        EmpInfo empInfo = matchingRepository.findEmpInfoByMemberId(id);

        //취업 등록 정보 empInfo DTO로 변환
        EmpEditForm empEditForm = EmpEditForm.builder()
                .id(empInfo.getId())
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
                .member(empInfo.getMember())
                .build();

        return empEditForm;
    }

    @Override
    public UploadFile findUploadFileById(Long id) {
        return matchingRepository.findUploadFileById(id);
    }

    @Override
    public String jsonEmpMapProfileDtoList() throws JsonProcessingException {
        //mapData를 담을 리스트 (JSON 문자열로 변환)
        List<Map<String, Double>> mapDataList = new ArrayList<>();
        //최근에 등록된 취업 현황 정보 1,000개
        List<EmpMapProfileDto> empInfoTop1000 = matchingRepository.findEmpInfoTop1000();
        //취업정보의 좌표(lat, lng)와 memberId를 Map형태로 저장 후 mapDataList에 추가
        for (EmpMapProfileDto mapProfileDto : empInfoTop1000) {
            Map<String, Double> mapData = new LinkedHashMap<String, Double>();
            mapData.put("lat", Double.valueOf(mapProfileDto.getLocation_api_lat()));
            mapData.put("lng", Double.valueOf(mapProfileDto.getLocation_api_lng()));
            mapData.put("user_num", Double.valueOf(mapProfileDto.getMember().getId()));
            mapDataList.add(mapData);
        }
        //카카오 지도에서 원하는 형태의 제이슨 문자열 생성
        ObjectMapper mapper= new ObjectMapper();
        String jsonMapData = "{\"positions\": ";
        jsonMapData += mapper.writeValueAsString(mapDataList);
        jsonMapData +="}";

        return jsonMapData;
    }

    @Override
    public List<Long> empMemberIdList() {

        return matchingRepository.findEmpMemberIdTop1000();
    }

    @Override
    public String jsonEmpInfoTop100List() throws JsonProcessingException {

        List<EmpInfo> empInfoTop100 = matchingRepository.findEmpInfoTop100();
        ObjectMapper mapper= new ObjectMapper();
        String jsonEmpList = mapper.writeValueAsString(empInfoTop100);
        return jsonEmpList;
    }


}
