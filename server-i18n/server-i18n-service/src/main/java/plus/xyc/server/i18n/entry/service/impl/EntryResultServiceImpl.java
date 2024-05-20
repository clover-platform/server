package plus.xyc.server.i18n.entry.service.impl;

import plus.xyc.server.i18n.entry.entity.dto.EntryResult;
import plus.xyc.server.i18n.entry.mapper.EntryResultMapper;
import plus.xyc.server.i18n.entry.service.EntryResultService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 翻译结果 服务实现类
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
@Service
public class EntryResultServiceImpl extends ServiceImpl<EntryResultMapper, EntryResult> implements EntryResultService {

    @Override
    public List<EntryResult> getLastResults(List<Long> ids, String language) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return baseMapper.getLastResults(ids, language);
    }

    @Override
    public List<EntryResult> getResults(List<Long> ids, String language) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return baseMapper.getResults(ids, language);
    }
}
