package life.majiang.community.dto;

import lombok.Data;

@Data
public class FileUploadDTO {
    private Integer success;
    private String message;
    private String url;
}
