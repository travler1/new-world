package myproject.web.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

@Component
@Slf4j
public class ImageView extends AbstractView {

    @Override
    public void renderMergedOutputModel(Map<String, Object> model,
                                        HttpServletRequest request,
                                        HttpServletResponse response) throws Exception {
        String uploadFolderPath;

        //파일읽기(사용자 지정 프로필이 없는 경우)
        if(model.get("basicProfile") != null && model.get("basicProfile").equals("yes")){
            uploadFolderPath = "src/main/resources/static/images/pageMain/";
        }else{
            //파일읽기(사용자 지정 프로필이 있는 경우)
            uploadFolderPath = "src/main/resources/static/images/userProfile/";
        }

        String filename = (String)model.get("filename");
        File file = new File(uploadFolderPath + filename);
        Resource resource = new FileSystemResource(file);
        //Resource file = new ClassPathResource("upload/" + filename);

        log.info("프로필 이미지 출력 : {}", file.toString());

        String ext = filename.substring(filename.lastIndexOf("."));
        if(ext.equalsIgnoreCase(".gif")){
            ext="image/gif";
        }else if(ext.equalsIgnoreCase(".png")){
            ext="image/png";
        }else{
            ext="image/jpeg";
        }

        response.setContentType(ext);
        response.setContentLengthLong(file.length());

        String fileName = new String(filename.getBytes("utf-8"), "iso-8859-1");

        response.setHeader("Content-Disposition",
                "attachment; filename=\"" + fileName + "\";");
        response.setHeader("Content-Transfer-Encoding", "binary");

        try (OutputStream out = response.getOutputStream();
             InputStream input = resource.getInputStream()) {
            IOUtils.copy(input, out);
            out.flush();
        }
    }
}
