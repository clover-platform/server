package plus.xyc.server.i18n.file.mapper;

import plus.xyc.server.i18n.file.entity.dto.File;
import plus.xyc.server.i18n.file.entity.request.FileListRequest;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 分支 Mapper 接口
 * </p>
 *
 * @author generator
 * @since 2025-06-17
 */
public interface FileMapper extends BaseMapper<File> {

    List<File> list(@Param("request") FileListRequest request);
    File findOneByModuleIdAndNameAndDeleted(@Param("moduleId") Long moduleId, @Param("name") String name, @Param("deleted") Boolean deleted);

}
