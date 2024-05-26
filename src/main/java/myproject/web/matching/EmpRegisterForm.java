package myproject.web.matching;

import jakarta.persistence.Lob;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import myproject.domain.member.EmbeddedDate;
import myproject.web.file.UploadFile;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
public class EmpRegisterForm {

    //회사 규모[스타트업/중소/중견/대기업]
    @NotNull
    private String comSize;
    //회사 인력 규모[5~10인/10~30인/30~50인/50~100인]
    @NotNull
    private String comPeople;
    //지원분야 [SI/SM/솔루션/서비스/기타/기타(IT외)
    @NotNull
    private String field;
    //직무 [백엔드/프론트엔드/풀스택/데브옵스/임베디드/DBA/기타]
    @NotNull
    private String role;
    //지원형태 [신입/중고신입/기타]
    @NotNull
    private String career;
    //연봉정보 공개유무 [공개/비공개]
    @NotNull
    private String salary_status;
    //연봉 [~2,600만원/2,600~3,000만원/3,000~3,400만원/3,400~3,800만원/3,800~4,200만원/4,200만원~]
    @NotNull
    private String salary;
    //수료 후 준비기간 [수료 전/수료 후 1개월 이내/수료 후 1개월~3개월/수료 후 3개월~6개월/수료 후 6개월~1년/수료 후 1년~]
    @NotNull
    private String periodTime;
    //학력 [고졸/전문대졸/대졸(수도권)/대졸(비수도권)/대학원/기타]
    @NotNull
    private String education;
    //컴퓨터계열 전공 유무 [전공/비전공]
    @NotNull
    private String major;
    //취업 당시 보유 자격증 [없음/정보처리기사(필기)/정보처리기사(필기+실기)/SQLD/AWS/기타]
    @NotNull
    private String certification;
    //취업한 회사 지역 [구로/가산 디지털단지 / 강남/판교/DMC(상암)/영등포/시청,종로/기타]
    @NotNull(message = "회사 지역을 선택해주세요.")
    private String location;
    private String location_api;
    private String location_api_lat;
    private String location_api_lng;
    //취업일시

    @NotNull
    private String workStart;

    @NotNull(message = "취업 합격 문자나 이메일을 캡처 후 첨부해주세요")
    private MultipartFile multipartFile;

    @NotNull
    private String advice;



}
