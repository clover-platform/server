package plus.xyc.server.i18n.entry.service.impl;

import plus.xyc.server.i18n.entry.entity.dto.EntryState;
import plus.xyc.server.i18n.entry.entity.request.EntryCountRequest;
import plus.xyc.server.i18n.entry.mapper.EntryStateMapper;
import plus.xyc.server.i18n.entry.service.EntryStateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 词条翻译状态 服务实现类
 * </p>
 *
 * @author generator
 * @since 2024-05-24
 */
@Service
public class EntryStateServiceImpl extends ServiceImpl<EntryStateMapper, EntryState> implements EntryStateService {

    @Override
    public Long countTranslated(EntryCountRequest request) {
        return baseMapper.countTranslated(request);
    }

    @Override
    public Long countVerified(EntryCountRequest request) {
        return baseMapper.countVerified(request);
    }
}
