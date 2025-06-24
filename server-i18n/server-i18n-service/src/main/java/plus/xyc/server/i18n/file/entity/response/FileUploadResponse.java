package plus.xyc.server.i18n.file.entity.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import plus.xyc.server.i18n.file.entity.request.FileUploadRequest;

@Data
@Schema(description = "上传文件响应")
public class FileUploadResponse {

    @Schema(description = "未成功文件")
    private List<FileUploadRequest.FileItem> files;

}