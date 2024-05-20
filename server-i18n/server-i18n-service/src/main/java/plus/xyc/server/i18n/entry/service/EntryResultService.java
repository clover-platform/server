package plus.xyc.server.i18n.entry.service;

import plus.xyc.server.i18n.entry.entity.dto.EntryResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 翻译结果 服务类
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
public interface EntryResultService extends IService<EntryResult> {

    List<EntryResult> getLastResults(List<Long> ids, String language);
    List<EntryResult> getResults(List<Long> ids, String language);

}
