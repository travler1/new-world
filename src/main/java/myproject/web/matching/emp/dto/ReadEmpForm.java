package myproject.web.matching.emp.dto;

import com.querydsl.core.annotations.QueryProjection;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import myproject.domain.member.EmbeddedDate;
import myproject.domain.member.Member;
import myproject.web.file.UploadFile;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ReadEmpForm {
    private Long id;
    //회사 규모[스타트업/중소/중견/대기업]
    private String comSize;
    //회사 인력 규모[5~10인/10~30인/30~50인/50~100인]
    private String comPeople;
    //지원분야 [SI/SM/솔루션/서비스/기타/기타(IT외)
    private String field;
    //직무 [백엔드/프론트엔드/풀스택/데브옵스/임베디드/DBA/기타]
    private String role;
    //지원형태 [신입/중고신입/기타]
    private String career;
    //연봉정보 공개유무 [공개/비공개]
    private String salary_status;
    //연봉 [~2,600만원/2,600~3,000만원/3,000~3,400만원/3,400~3,800만원/3,800~4,200만원/4,200만원~]
    private String salary;
    //수료 후 준비기간 [수료 전/수료 후 1개월 이내/수료 후 1개월~3개월/수료 후 3개월~6개월/수료 후 6개월~1년/수료 후 1년~]
    private String periodTime;
    //학력 [고졸/전문대졸/대졸(수도권)/대졸(비수도권)/대학원/기타]
    private String education;
    //컴퓨터계열 전공 유무 [전공/비전공]
    private String major;
    //취업 당시 보유 자격증 [없음/정보처리기사(필기)/정보처리기사(필기+실기)/SQLD/AWS/기타]
    private String certification;
    //취업한 회사 지역 [구로/가산 디지털단지 / 강남/판교/DMC(상암)/영등포/시청,종로/기타]
    private String location;
    private String location_api;
    private String location_api_lat;
    private String location_api_lng;
    //취업일시
    private String workStart;
    private String advice;
    private UploadFile uploadFile;
    private EmbeddedDate date;
    private Member member;

    @QueryProjection
    public ReadEmpForm(Long id, String comSize, String comPeople, String field, String role, String career, String salary_status, String salary, String periodTime, String education, String major, String certification, String location, String location_api, String location_api_lat, String location_api_lng, String workStart, String advice, UploadFile uploadFile, EmbeddedDate date, Member member) {
        this.id = id;
        this.comSize = comSize;
        this.comPeople = comPeople;
        this.field = field;
        this.role = role;
        this.career = career;
        this.salary_status = salary_status;
        this.salary = salary;
        this.periodTime = periodTime;
        this.education = education;
        this.major = major;
        this.certification = certification;
        this.location = location;
        this.location_api = location_api;
        this.location_api_lat = location_api_lat;
        this.location_api_lng = location_api_lng;
        this.workStart = workStart;
        this.advice = advice;
        this.uploadFile = uploadFile;
        this.date = date;
        this.member = member;
    }
}
