package myproject.web.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class FileStore {

    @Value("${file.profileImage.dir}")
    public String profileImageDir;

    @Value("${file.advice.dir}")
    public String adviceDir;

    @Value("${file.board.dir}")
    public String boardDir;

    @Value("${file.emp.dir}")
    public String empDir;

    public String getProfileImageFullPath(String profileImageName) {
        return profileImageDir + profileImageName;
    }

    public String getAdviceFullPath(String adviceName) {
        return adviceDir + adviceName;
    }

    public String getFullImagePath(FileCategory fileCategory, String fileName) {
        switch (fileCategory.name()) {
            case "PROFILE_IMAGE" : return profileImageDir + fileName;
            case "ADVICE" : return adviceDir + fileName;
            case "BOARD" : return boardDir + fileName;
            case "EMP_INFO" : return empDir + fileName;
            default: return null;
        }
    }

    //파일 여러건 저장
    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles, FileCategory fileCategory) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if(!multipartFile.isEmpty()) {
                /*UploadFile uploadFile = storeFile(multipartFile);
                storeFileResult.add(uploadFile); 얘도 코드를 한 줄로 합칠거임 ctrl + alt + N*/
                storeFileResult.add(storeFile(multipartFile, fileCategory));
            }
        }
        return storeFileResult;
    }

    //파일 단건 저장
    public UploadFile storeFile(MultipartFile multipartFile, FileCategory fileCategory) throws IOException {
        log.info("storeFile 메서드 진입1");
        if (multipartFile.isEmpty()) {
            return null;
        }
        log.info("storeFile 메서드 진입2");

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);

        switch (fileCategory.name()){
            case "PROFILE_IMAGE" :
                multipartFile.transferTo(new File(profileImageDir + storeFileName));
                break;
            case "EMP_INFO" :
                multipartFile.transferTo(new File(empDir + storeFileName));
                break;
            case "BOARD" :
                multipartFile.transferTo(new File(boardDir + storeFileName));
                break;
            case "ADVICE" :
                multipartFile.transferTo(new File(adviceDir + storeFileName));
                break;
        }

        return new UploadFile(originalFilename, storeFileName);

        /*int pos = originalFilename.lastIndexOf(".");
        String ext = originalFilename.substring(pos + 1); 얘네를 메서드로 만들거임. 두 줄 드래그하고 ctrl+alt + m*/

        //서버에 저장하는 파일명 (but, 확장자명은 살려두고싶음.)
        /*String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFilename);
        String storeFileName = uuid + "." + ext; 여기도 별도의 메서드로 뽑아줄거임. originalFileName을 넘기면 storeFileName을 반환하게끔.*/

    }

    private String createStoreFileName(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFilename);
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    //파일 삭제
    public boolean deleteFile(UploadFile uploadFile, FileCategory fileCategory) {
        File file = new File(fileCategory.name() + uploadFile.getStoreFileName());
        return file.delete();
    }
}
