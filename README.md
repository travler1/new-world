# Hello-world
# 개인 프로젝트 : Hello World!
#### 이름 : 서현진
#### 개발 기간 : ( 2024-05-29 ~ 2024-06-10 )
#### 프로젝트 아이디어 개요 : 
학원 수업을 진행하면서 느낀 궁금점이 있었다. 수료한 학생들이 취업하는 회사들의 정보와 그들과의 커뮤니케이션이 필요했다. 

학원 수강 후 개발 일을 시작한 현직자와 현재 학원 수업을 진행 중인 학생을 연결시켜주고 어떠한 회사로 취업했는지

취업 당시 자격증은 어떤 것들이 있었는지, 취업한 회사의 지역은 어디인지 다양한 정보를 제공해준다.

#### 운영체제 : Window10
#### 데이터베이스 : MySql
#### ORM 프레임워크 : JPA ( Spring data JPA , QueryDSL )
#### 프레임워크 : Spring Boot ( Gradle )
#### 도구 및 언어 : Eclipse | Java, , Thymeleaf, JQuery, Ajax, JavaScript, CSS, HTML, 
#### 외부 라이브러리 : BootStrap, KAKAO Map API, CKEditor, Tiny Edito, Google chart.js, EChart.js

## 주요 기능
+ 회원가입기능(이메일,비밀번호,닉네임 외 선택추가사항)/현직자 등록시 간단한 입력 폼 작성
+ 관리자가 일반회원(수강생)->현직자 회원등급 변경
+ Kakao API를 활용한 지도 api 구현(마커클러스터러+커스텀오버레이)
+ 현직자 등록시 입력한 정보들을 도넛차트로 산출
+ 웹소켓을 이용한 실시간 채팅
+ 유저 간 첨삭기능(쪽지기능에 파일 첨부)
+ 마이페이지에서 주고 받은 첨삭 확인 및 답장기능
+ ck에디터를 활용한 게시판(CRUD)기능 좋아요/댓글
---
## 시연 결과
|시연|결과화면|
|---|---|
|메인 페이지|![메인페이지](https://github.com/travler1/Hello-World/blob/master/%EB%A9%94%EC%9D%B8.jpg) |
|로그인 페이지|![로그인페이지](https://github.com/travler1/Hello-World/blob/master/%EB%A1%9C%EA%B7%B8%EC%9D%B8.jpg)|
|취업현황 페이지| ![취업현황페이지](https://github.com/travler1/Hello-World/blob/master/%EC%B7%A8%EC%97%85%ED%98%84%ED%99%A9.jpg) ![레벨축소](https://github.com/travler1/Hello-World/blob/master/%EB%A0%88%EB%B2%A8%EC%B6%95%EC%86%8C%EC%B9%B4%EC%B9%B4%EC%98%A4%EB%A7%B5.jpg)|
|현직자 신청하기 폼|![현직자신청하기폼](https://github.com/travler1/Hello-World/blob/master/%ED%98%84%EC%A7%81%EC%9E%90%20%EC%8B%A0%EC%B2%AD%ED%95%98%EA%B8%B0%20%ED%8F%BC.jpg)|
|현직자프로필|![현직자프로필](https://github.com/travler1/Hello-World/blob/master/%ED%98%84%EC%A7%81%EC%9E%90%ED%94%84%EB%A1%9C%ED%95%84.jpg)|
|첨삭기능|![첨삭기능](https://github.com/travler1/Hello-World/blob/master/%EC%B2%A8%EC%82%AD%EA%B8%B0%EB%8A%A5.png)|
|채팅기능|![채팅기능](https://github.com/travler1/Hello-World/blob/master/%EC%B1%84%ED%8C%85%EA%B8%B0%EB%8A%A5.jpg)|
|게시판 메인페이지|![게시판 메인](https://github.com/travler1/new-world/assets/153168650/4aa8bba7-eb46-4bcd-adf4-25f2e96e5dda)|
|게시판 글쓰기|![게시판글쓰기](https://github.com/travler1/Hello-World/blob/master/%EA%B2%8C%EC%8B%9C%ED%8C%90%EA%B8%80%EC%93%B0%EA%B8%B0.jpg)|
|게시판 글상세|![게시판글상세](https://github.com/travler1/Hello-World/blob/master/%EA%B2%8C%EC%8B%9C%ED%8C%90%EA%B8%80%EC%83%81%EC%84%B8.jpg)|
|JPA ORM|![myproject_jpa_erd](https://github.com/travler1/new-world/assets/153168650/ba2b3a19-edef-46d4-a776-e51e41011ef3)|
|프로젝트 중 어려웠던 점|**페이징 + 검색기능** <br> 기존 마이바티스(Oracle Dialect)를 이용하던 방식에서 JPA의 QueryDsl을 사용하여 페이징처리와 검색기능을 구현하는 것이 어려웠고, 게시판 조회 시 게시판에 딸린 댓글 수만큼 같은 게시글이 중복 출력되는 문제 발생. |
|해결 방안|Distinct()로 해결, 게시판과 댓글을 조인할 때 게시물 하나에 댓글 수만큼 쿼리가 조회되는 문제를 distinct()로 해결 <br>![페이징 문제 해결](https://github.com/travler1/new-world/assets/153168650/69d11efd-1007-4894-b34b-96edd2dcecf4)|
|프로젝트 중 어려웠던 점|**중복 사용되는 메서드** <br>- Post method 로 로직 처리시 중복되는 처리를 피하기 위해 PRG(post – redirect – get )<br>도입하여 처리가 완료되었다는 공통 폼의 호출 후에 get 메서드 호출하게 되는데<br>반복되는 공통 폼 호출로 인해 코드가 간결하지 않고, 유지보수에도 좋지 못함.<br>- 회원만 이용할 수 있는 서비스의 경우 회원 엔티티를 조회하는 로직 반복사용 |
|해결방안|- 반복 사용하는 메서드를 모아놓은 Util 클래스 생성 후 반복 사용 메서드 선언 <br>![중복1](https://github.com/travler1/new-world/assets/153168650/34a3b147-69bc-4c35-8be5-e77cb39d6373)<br>-LoginAccount 커스텀인터페이스 생성 후 ArgumentResolver 등록, 이후 컨트롤러의 매개변수로 호출 <br>![중복2](https://github.com/travler1/new-world/assets/153168650/754201c5-b68a-4c6b-a13f-8de3267e51cb)<br>![loginAccountArgumentResolver](https://github.com/travler1/new-world/assets/153168650/ce190efe-3286-470b-a0a2-92349818e8ad)|
|프로젝트 중 어려웠던 점|**파일 업로드 시 폴더 하나에 모든 파일이 업로드 되는 문제 **|
|해결방안|파일 업로드 시 목적에 따른 폴더를 세분화하여 각각의 폴더에 업로드<br>![file경로](https://github.com/travler1/new-world/assets/153168650/4e50ddaf-08bc-4e94-940b-b24d080af0a4)<br><<application.yml 에서 경로 설정>> <<FileStore 파일에서 변수로 설정>><br> ![filedownload](https://github.com/travler1/new-world/assets/153168650/017cef32-aa68-489a-9341-05ae5dccceec)<br>FileCategory enum 타입 설정, 파일 업로드 시 업로드 종류에 따라 각기 다른 폴더에<br>파일 저장. 파일 다운로드 시에도 각기 다른 파일 경로로부터 파일 첨부. |
|기존 프로젝트와<br>차별점 |Hello – World 프로젝트는 기존 프로젝트를 기능 확장하여 새로 만든 프로젝트입니다. <br> 기존 프로젝트와의 차별점 및 개선점은 다음과 같습니다. <br> **1. ORM 프레임워크로 JPA 를 사용했습니다.**  <br>기존 데이터엑세스 프레임워크로 MyBatis 를 이용하면서 일일이  <br>쿼리를 작성해줘야 했던 번거로움이 사라졌습니다. <br>**2. 엔티티 노출 최소화**<br>기존엔 vo 클래스의 엔티티를 그대로 컨트롤러계층까지 api 에 그대로 사용했습니다.<br>이로 인한 데이터 노출과 보안위험, 그리고 엔티티의 변경이 클라이언트에 영향을 미쳐<br>유지보수가 어렵고, 검증로직도 섞여 지저분한 엔티티가 되기 때문에 api 에 맞는 DTO 를<br>만들고 @Builder 를 사용하여 빌더패턴으로 엔티티<->DTO 를 변환했습니다.<br>또한, 통일된 패턴의 이름을 사용하려 노력했습니다. <br>![form통일](https://github.com/travler1/new-world/assets/153168650/40fcfe1e-181f-49d4-9ef3-283c48c0d6d0)<br>**3. 컨트롤러의 경량화**<br>기존에 컨트롤러의 모든 비즈니스 로직과 프레젠테이션로직을 처리해 유지보수가 굉장히<br>어렵고 객체의 역할과 책임의 분리에 어긋났습니다. 컨트롤러에선 어떤 기능을<br>담당하는지 최소한의 로직만 가지고 있고, 나머지는 레포지토리와 서비스 계층에서<br>처리했고, 메서드 이름을 통해 명확히 어떤 기능을 담당하는지를 알 수 있도록 네이밍에<br>신중히 노력했습니다. <br>|

**이후 학업 계획**<br>
|---|---|---|
|N|언어|이유|
|1|스프링 시큐리티|사용자 인증, 권한 부여, 보안 설정, 소셜 로그인 등을 간편하게 처리할 수 있고<br>개발 생산성을 높이고 보안 취약점을 방지 가능.|
|2|Docker|개발된 소프트웨어를 유지보수, 이동, 조립할 수 있게 도와주는 플랫폼 사용 방법<br>학습 예정 |
|3|nginx|웹 서버의 유지 및 업데이트 시 서비스 중단을 최소화하고 배포할 수 있어사용자에게 지속적인 서비스를 제공할 수 있음. |





