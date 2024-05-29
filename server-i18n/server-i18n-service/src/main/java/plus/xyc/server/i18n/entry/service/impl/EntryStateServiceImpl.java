package plus.xyc.server.i18n.entry.service.impl;

import jakarta.annotation.Resource;
import org.zkit.support.starter.redisson.DistributedLock;
import plus.xyc.server.i18n.entry.entity.dto.Entry;
import plus.xyc.server.i18n.entry.entity.dto.EntryResult;
import plus.xyc.server.i18n.entry.entity.dto.EntryState;
import plus.xyc.server.i18n.entry.entity.request.EntryCountRequest;
import plus.xyc.server.i18n.entry.mapper.EntryMapper;
import plus.xyc.server.i18n.entry.mapper.EntryResultMapper;
import plus.xyc.server.i18n.entry.mapper.EntryStateMapper;
import plus.xyc.server.i18n.entry.service.EntryStateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import plus.xyc.server.i18n.module.service.ModuleCountService;

import java.util.Date;

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

    @Resource
    private ModuleCountService moduleCountService;
    @Resource
    private EntryMapper entryMapper;
    @Resource
    private EntryResultMapper entryResultMapper;

    @Override
    public Long countTranslated(EntryCountRequest request) {
        return baseMapper.countTranslated(request);
    }

    @Override
    public Long countVerified(EntryCountRequest request) {
        return baseMapper.countVerified(request);
    }

    private EntryState getByEntryIdAndLanguage(Long entryId, String language) {
        EntryState state = baseMapper.findOneByEntryIdAndLanguage(entryId, language);
        if(state == null) {
            state = new EntryState();
            state.setEntryId(entryId);
            state.setLanguage(language);
            save(state);
        }
        return state;
    }

    @Override
    @DistributedLock(value = "i18n:entry:state", key = "#entryId")
    public void translate(Long entryId, String language, Long resultId) {
        EntryState state = getByEntryIdAndLanguage(entryId, language);
        state.setResultId(resultId);
        state.setTranslated(true);
        updateById(state);

        Entry entry = entryMapper.selectById(entryId);

        // 更新统计
        moduleCountService.updateCount(entry.getModuleId(), entry.getBranchId(), language);
    }

    @Override
    @DistributedLock(value = "i18n:entry:state", key = "#entryId")
    public void removeTranslate(Long entryId, String language, Long resultId) {
        EntryState state = getByEntryIdAndLanguage(entryId, language);
        EntryResult result = entryResultMapper.getLastResult(entryId, language);
        if(result == null) {
            state.setResultId(null);
            state.setTranslated(false);
            state.setVerified(false);
        }else{
            state.setResultId(result.getId());
            state.setTranslated(true);
            if(result.getVerified()) {
                state.setVerified(true);
            }
        }
        updateById(state);
        Entry entry = entryMapper.selectById(entryId);
        // 更新统计
        moduleCountService.updateCount(entry.getModuleId(), entry.getBranchId(), language);
    }

    @Override
    @DistributedLock(value = "i18n:entry:state", key = "#entryId")
    public void approve(Long entryId, String language, Long resultId) {
        EntryState state = getByEntryIdAndLanguage(entryId, language);
        if(state.getEntryId().equals(entryId)) {
            state.setVerified(true);
            state.setVerificationTime(new Date());
            updateById(state);
        }

        Entry entry = entryMapper.selectById(entryId);
        // 更新统计
        moduleCountService.updateCount(entry.getModuleId(), entry.getBranchId(), language);
    }
}
