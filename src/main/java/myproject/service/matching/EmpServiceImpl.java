package myproject.service.matching;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.domain.matching.emp.EmpInfo;
import myproject.web.matching.emp.dto.*;
import myproject.domain.matching.emp.EmpRepository;
import myproject.domain.member.EmbeddedDate;
import myproject.domain.member.Member;
import myproject.service.member.MemberService;
import myproject.web.file.FileCategory;
import myproject.web.file.FileStore;
import myproject.web.file.UploadFile;
import myproject.web.member.MemberDTO.SessionMemberForm;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static myproject.Util.getLoginMemberId;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EmpServiceImpl implements EmpService {

    private final EmpRepository empRepository;
    private final MemberService memberService;
    private final FileStore fileStore;

    //취업 정보 저장
    @Override
    public void registerEmp(SaveEmpForm saveEmpForm, Member member) throws IOException {

        UploadFile uploadFile = null;
        try{
            //EmpInfo의 첨부파일 저장
            uploadFile = fileStore.storeFile(saveEmpForm.getMultipartFile(), FileCategory.EMP_INFO);
            //saveEmpForm -> empInfo 엔티티 변환
            EmpInfo empInfo = saveEmpForm.toEntity(member, uploadFile);
            //empInfo 데이터 저장
            empRepository.save(empInfo);
        }catch(Exception e){
            log.info("EmpInfo 파일 저장 실패");
            if(uploadFile != null){
                fileStore.deleteFile(uploadFile, FileCategory.EMP_INFO);
            }
            throw e;
        }
    }

    //로그인 회원의 취업 정보 조회
    //@Override
    public ReadEmpForm findMyEmp(Member member) {

        ReadEmpForm empInfoByMemberId = empRepository.findEmpInfoByMemberId(member.getId());

        return empInfoByMemberId;

    }

    @Override //다른 회원의 취업정보 조회
    public ReadEmpForm findEmpByMemberId(Long id) {

        //취업 등록 정보 empInfo DTO로 변환
        ReadEmpForm empInfoByMemberId = empRepository.findEmpInfoByMemberId(id);

        return empInfoByMemberId;
    }

    //취업인증파일 조회
    @Override
    public UploadFile findUploadFileById(Long id) {
        return empRepository.findUploadFileById(id);
    }

    //카카오맵에 출력할 empInfo의 지도 위치 json으로 반환
    @Override
    public String jsonEmpMapProfileDtoList() throws JsonProcessingException {
        //mapData를 담을 리스트 (JSON 문자열로 변환)
        List<Map<String, Double>> mapDataList = new ArrayList<>();
        //최근에 등록된 취업 현황 정보 1,000개
        List<EmpMapProfileForm> empInfoTop1000 = empRepository.findEmpInfoTop1000();
        //취업정보의 좌표(lat, lng)와 memberId를 Map형태로 저장 후 mapDataList에 추가
        for (EmpMapProfileForm mapProfileDto : empInfoTop1000) {
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

        return empRepository.findEmpMemberIdTop1000();
    }

    @Override
    public String jsonEmpInfoTop100List() throws JsonProcessingException {

        List<JsonCharEmpForm> empCharInfoTop1000 = empRepository.findEmpCharInfoTop1000();

        ObjectMapper mapper= new ObjectMapper();
        String jsonEmpList = mapper.writeValueAsString(empCharInfoTop1000);
        return jsonEmpList;
    }


}
