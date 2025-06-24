package plus.xyc.server.i18n.entry.service.impl;

import jakarta.annotation.Resource;
import org.zkit.support.starter.redisson.DistributedLock;
import plus.xyc.server.i18n.entry.entity.dto.EntryResult;
import plus.xyc.server.i18n.entry.entity.dto.EntryState;
import plus.xyc.server.i18n.entry.entity.request.EntryCountRequest;
import plus.xyc.server.i18n.entry.mapper.EntryMapper;
import plus.xyc.server.i18n.entry.mapper.EntryResultMapper;
import plus.xyc.server.i18n.entry.mapper.EntryStateMapper;
import plus.xyc.server.i18n.entry.service.EntryStateService;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import plus.xyc.server.i18n.module.service.ModuleCountService;

import java.util.Collections;
import java.util.Date;
import java.util.List;

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
    @DistributedLock(value = "'i18n:entry:state:'+#entryId")
    public void translate(Long entryId, String language, Long resultId) {
        EntryState state = getByEntryIdAndLanguage(entryId, language);
        state.setResultId(resultId);
        state.setTranslated(true);
        updateById(state);
    }

    @Override
    @DistributedLock(value = "'i18n:entry:state:'+#entryId")
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
            state.setVerified(result.getVerified());
        }
        updateById(state);
    }

    @Override
    @DistributedLock(value = "'i18n:entry:state:'+#entryId")
    public void approve(Long entryId, String language, Long resultId) {
        EntryState state = getByEntryIdAndLanguage(entryId, language);
        lambdaUpdate().set(EntryState::getVerified, true)
                .set(EntryState::getVerificationTime, new Date())
                .set(EntryState::getResultId, resultId)
                .eq(EntryState::getId, state.getId())
                .update();
    }

    @Override
    public void removeApproval(Long entryId, String language, Long resultId) {
        EntryState state = getByEntryIdAndLanguage(entryId, language);
        lambdaUpdate().set(EntryState::getVerified, false)
                .set(EntryState::getResultId, resultId)
                .eq(EntryState::getId, state.getId())
                .update();
    }

    @Override
    public List<EntryState> findListByEntryIds(List<Long> entryIds) {
        if(entryIds == null || entryIds.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<EntryState> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(EntryState::getEntryId, entryIds);
        return list(queryWrapper);
    }

}
