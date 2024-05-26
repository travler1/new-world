package myproject.domain.matching;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import myproject.domain.member.EmbeddedDate;
import myproject.domain.member.Member;
import myproject.web.file.UploadFile;
import myproject.web.matching.EmpRegisterForm;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class EmpInfo {

    @Id @GeneratedValue
    @Column(name = "empInfo_id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "empInfo")
    private Member member;

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

    private EmbeddedDate date;

    private UploadFile uploadFile;
    @Lob
    private String advice;

    //dto->entity
    public EmpInfo createEmpInfo(EmpRegisterForm form, UploadFile uploadFile) {

        EmpInfo empInfo = new EmpInfo();

        empInfo.comSize=form.getComSize();
        empInfo.comPeople=form.getComPeople();
        empInfo.field=form.getField();
        empInfo.role=form.getRole();
        empInfo.career=form.getCareer();
        empInfo.salary_status=form.getSalary_status();
        empInfo.salary=form.getSalary();
        empInfo.periodTime=form.getPeriodTime();
        empInfo.education=form.getEducation();
        empInfo.major=form.getMajor();
        empInfo.certification=form.getCertification();
        empInfo.location=form.getLocation();
        empInfo.location_api=form.getLocation_api();
        empInfo.location_api_lat=form.getLocation_api_lat();
        empInfo.location_api_lng=form.getLocation_api_lng();
        empInfo.workStart=form.getWorkStart();
        empInfo.date = new EmbeddedDate();
        empInfo.date.setReg_date(new Date());
        empInfo.uploadFile=uploadFile;
        empInfo.advice=form.getAdvice();

        return empInfo;
    }

    //연관관계 편의 메서드
    public void SetMember(Member member) {
        this.member = member;
        member.setEmpInfo(this);
    }

    @Builder
    public EmpInfo(String comSize, String comPeople, String field, String role, String career, String salary_status, String salary, String periodTime, String education, String major, String certification, String location, String location_api, String location_api_lat, String location_api_lng, String workStart, EmbeddedDate date, UploadFile uploadFile, String advice, Member member) {
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
        this.date = date;
        this.uploadFile = uploadFile;
        this.advice = advice;

        //==연관관계 편의 메서드==//
        this.member = member;
        member.setEmpInfo(this);
    }
}
