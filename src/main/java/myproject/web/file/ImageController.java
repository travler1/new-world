package myproject.web.file;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.LoginAccount;
import myproject.domain.board.entity.Board;
import myproject.domain.matching.advice.entity.Advice;
import myproject.service.matching.advice.AdviceService;
import myproject.domain.member.Member;
import myproject.service.board.BoardService;
import myproject.service.matching.EmpService;
import myproject.domain.member.repository.MemberRepository;
import myproject.service.member.MemberService;
import myproject.web.member.MemberDTO.SessionMemberForm;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ImageController {

    private final MemberRepository memberRepository;
    private final FileStore fileStore;
    private final EmpService empService;
    private final MemberService memberService;
    private final BoardService boardService;
    private final AdviceService adviceService;

    //view -> 프로필 이미지 출력
    @ResponseBody
    @GetMapping("/member/photoView")
    public Resource userProfileImage(@LoginAccount Member loginMember) throws MalformedURLException {

        log.info("ImageController 진입");
        //로그인한 회원 조회

        String filePath;
        if (loginMember.getProfileImage() == null) {
            log.info("filePath진입");
            filePath = "classpath:/static/images/pageMain/face.png";
        } else {
            log.info("filePath진입2");
            filePath = "file:" + fileStore.getProfileImageFullPath(loginMember.getProfileImage().getStoreFileName());
        }
        return new UrlResource(filePath);
    }

    /*=========================
     *	      이미지 출력
     *=========================*/
    @ResponseBody
    @GetMapping("/photoView")
    public Resource photoView(@RequestParam("num") Long id, @RequestParam("category") String category,
                              @PathVariable(value = "id", required = false) Long boardId) throws MalformedURLException {

        String filePath = "file:";
        String defaultImagePath = "classpath:/static/images/pageMain/face.png";
        log.info("num={}, category={}", id, category);
        UploadFile uploadFile = null;

        switch (category) {
            case "EMP_INFO":
                uploadFile = empService.findUploadFileById(id);
                log.info("uploadFile = {}", uploadFile);
                filePath += fileStore.getFullPath(FileCategory.EMP_INFO, uploadFile.getStoreFileName());
                log.info("filePath = {}", filePath);
                break;
            case "BOARD":
                log.info("BOARD 접근");
                break;

            case "PROFILE_IMAGE":
                uploadFile = memberService.findUploadFileById(id);
                if (uploadFile != null) {
                    filePath += fileStore.getFullPath(FileCategory.PROFILE_IMAGE, uploadFile.getStoreFileName());
                } else {
                    filePath = defaultImagePath;
                }
                break;
        }

        log.info("filePath={}", filePath);
        return new UrlResource(filePath);
    }

    /*=========================
     *	  게시판 이미지 출력
     *=========================*/
    @ResponseBody
    @GetMapping("/upload/board/{id}")
    public Resource boardImage(@PathVariable("id") Long boardId) throws MalformedURLException {

        //게시판 첨부사진 불러오기
        String filePath = "file:";

        Board board = boardService.getBoardById(boardId);
        UploadFile uploadFile = board.getUploadFile();
        log.info("uploadFile = {}", uploadFile);

        filePath += fileStore.getFullPath(FileCategory.BOARD, uploadFile.getStoreFileName());
        log.info("filePath = {}", filePath);

        return new UrlResource(filePath);
    }

    /*=========================
     *	   프로필 이미지 업로드
     *=========================*/
    @PostMapping("/member/updateMyPhoto")
    @ResponseBody
    public Map<String, String> processProfile(HttpSession session,
                                              @RequestParam("upload") MultipartFile uploadImage) throws IOException {
        //ajax결과 반환을 위한 map생성
        Map<String, String> mapAjax = new HashMap<>();
        //로그인한 회원 조회
        SessionMemberForm sessionMemberForm = (SessionMemberForm) session.getAttribute("loginMember");
        if (sessionMemberForm == null) {
            mapAjax.put("result", "logout");
        } else {
            //기존에 저장된 프로필사진이 있는 경우 기존 프로필사진 삭제 후 저장.
            if (sessionMemberForm.getProfileImage() != null) {
                log.info("기존 프로필 사진이 있는 경우 로직 진입");
                fileStore.deleteFile(sessionMemberForm.getProfileImage(), FileCategory.PROFILE_IMAGE);
                memberRepository.deleteProfileById(sessionMemberForm.getId(), sessionMemberForm.getProfileImage());
            }
            //파일 저장
            UploadFile uploadFile = fileStore.storeFile(uploadImage, FileCategory.PROFILE_IMAGE);
            //멤버 엔티티에 파일 정보 저장
            sessionMemberForm.setProfileImage(uploadFile);
            log.info("프로필 이미지 업로드 메서드 진입");
            memberRepository.updateProfileById(sessionMemberForm.getId(), sessionMemberForm.getProfileImage());
            memberRepository.updateUploadFileById(sessionMemberForm.getId(), uploadFile);
            mapAjax.put("result", "success");
        }
        return mapAjax;
    }

    @GetMapping("/attach/{category}/{id}")
    public ResponseEntity<UrlResource> attachFileDownload(@PathVariable("category") String category,
                                                          @PathVariable("id") Long id,
                                                          @LoginAccount Member member)
                                                        throws MalformedURLException {

        String storeFileName = "";
        String uploadFileName = "";
        String getFullPath = "";

        switch (category) {
            case "board":
                Board board = boardService.getBoardById(id);
                storeFileName += board.getUploadFile().getStoreFileName();
                uploadFileName += board.getUploadFile().getUploadFileName();
                getFullPath += fileStore.getFullPath(FileCategory.BOARD, storeFileName);
                break;
            case "advice":
                //사용자가 해당 첨삭과 관련이 있는지 체크
                Boolean authCheck = adviceService.getAuthCheck(id, member.getId());
                if (authCheck == false) {
                    throw new IllegalStateException("첨삭 발신자 혹은 수신자만 다운로드 가능합니다.");
                }
                Advice advice = adviceService.getAdviceById(id);
                storeFileName += advice.getUploadFile().getStoreFileName();
                uploadFileName += advice.getUploadFile().getUploadFileName();
                getFullPath += fileStore.getFullPath(FileCategory.ADVICE, storeFileName);
                break;
        }

        if (getFullPath.equals("")) {
            throw new IllegalStateException("첨부파일이 존재하지 않습니다.");
        }

        UrlResource resource = new UrlResource("file:" + getFullPath);
        log.info("uploadFileName={}", uploadFileName);

        String encodedUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8); //인코딩 안하면 한글로 된 파일명 같은것들이 깨질 수 있음. //웹브라우저에 따라 조금씩 다를 수 있음.
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";

        //첨부파일로 다운받으려면 다음과 같이 헤더에 추가해야함. 이건 규약임.
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);

    }
}
