package myproject.web.file;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.service.matching.EmpService;
import myproject.domain.member.repository.MemberRepository;
import myproject.service.member.MemberService;
import myproject.web.member.MemberDTO.SessionMemberForm;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
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

    //view -> 프로필 이미지 출력
    @ResponseBody
    @GetMapping("/member/photoView")
    public Resource userProfileImage(HttpSession session) throws MalformedURLException {

        log.info("ImageController 진입");
        //로그인한 회원 조회
        SessionMemberForm loginMember = (SessionMemberForm) session.getAttribute("loginMember");

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

    @ResponseBody
    @GetMapping("/photoView")
    public Resource photoView(@RequestParam("num") Long id, @RequestParam("category") String category) throws MalformedURLException {

        String filePath = "file:";
        String defaultImagePath = "classpath:/static/images/pageMain/face.png";
        log.info("num={}, category={}", id, category);
        UploadFile uploadFile = null;
        switch (category) {
            case "EMP_INFO":
                uploadFile= empService.findUploadFileById(id);
                log.info("uploadFile = {}", uploadFile);
                filePath += fileStore.getFullImagePath(FileCategory.EMP_INFO, uploadFile.getStoreFileName());
                log.info("filePath = {}", filePath);
                break;
            case "BOARD":
                log.info("BOARD 접근");
                break;

            case "PROFILE_IMAGE":
                uploadFile= memberService.findUploadFileById(id);
                if (uploadFile != null) {
                     filePath += fileStore.getFullImagePath(FileCategory.PROFILE_IMAGE, uploadFile.getStoreFileName());
                }else{
                    filePath = defaultImagePath;
                }
                break;
        }
        log.info("filePath={}",filePath);
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
}
