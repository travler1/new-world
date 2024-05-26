package myproject.web.file;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UploadFile {

    private String uploadFileName; //저장할 파일 이름
    private String storeFileName; //구별을 위해 uuid로 저장된 파일 이름

    public UploadFile(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
}
