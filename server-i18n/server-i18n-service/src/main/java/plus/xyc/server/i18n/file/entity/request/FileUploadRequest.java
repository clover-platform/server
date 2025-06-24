package plus.xyc.server.i18n.file.entity.request;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
 
@Data
@Schema(description = "上传文件")
public class FileUploadRequest {

    @Schema(description = "模块ID", hidden = true)
    private Long moduleId;

    @Schema(description = "用户ID", hidden = true)
    private Long userId;

    @Schema(description = "文件ID")
    private Long fileId;

    @Schema(description = "文件")
    private List<FileItem> files;

    @Data
    @Schema(description = "文件项")
    public class FileItem {

        @Schema(description = "文件名")
        private String name;

        @Schema(description = "文件大小，单位字节")
        private Long size;

        @Schema(description = "文件类型")
        private String type;

        @Schema(description = "文件URL")
        private String url;

        @Schema(description = "是否重复")
        private Boolean repeated;
    }

}