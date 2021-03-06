package life.majiang.community.controller;

import life.majiang.community.dto.FileUploadDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;


@Controller
@Slf4j
public class FileController {

    @RequestMapping("/file/upload")
    @ResponseBody
    public FileUploadDTO upload(@RequestParam(value = "editormd-image-file", required = true) MultipartFile file) {
        FileUploadDTO fileUploadDTO = new FileUploadDTO();
        String destFileName= System.currentTimeMillis()+file.getOriginalFilename();
        try {

            File path = new File(ResourceUtils.getURL("classpath").getPath());
            File uploadFilePath = new File(path.getAbsolutePath(), "static"+File.separator+"upload"+File.separator+"images");
            if (!uploadFilePath.exists()) {
                log.info("{} 保存目录已经生成",uploadFilePath);
                uploadFilePath.mkdirs();
            }
            File destFile=new File(uploadFilePath.getAbsolutePath(),destFileName);
            if (!destFile.exists()) {
                log.info("{} 保存目录已经生成",destFile);
                destFile.mkdirs();
            }
            file.transferTo(destFile);
            log.info("{} 图片上传成功",destFileName);
            fileUploadDTO.setMessage("保存成功");
            fileUploadDTO.setSuccess(1);
            fileUploadDTO.setUrl("http://localhost:8887"+File.separator+"upload"+File.separator+"images"+File.separator+destFileName);

        } catch (Exception e) {
            e.printStackTrace();
            log.info("{} 图片上传失败",destFileName);
            fileUploadDTO.setMessage("上传失败");
            fileUploadDTO.setSuccess(0);
        }
        return fileUploadDTO;
    }
}
