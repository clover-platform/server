package plus.xyc.server.i18n.entry.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.aop.framework.AopContext;
import org.springframework.aop.framework.AopProxy;
import org.springframework.data.redis.core.RedisTemplate;
import org.zkit.support.starter.mybatis.entity.PageQueryRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import plus.xyc.server.i18n.entry.entity.dto.Entry;
import plus.xyc.server.i18n.entry.entity.dto.EntryResult;
import plus.xyc.server.i18n.entry.entity.mapstruct.EntryMapStruct;
import plus.xyc.server.i18n.entry.entity.request.EntryCountRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryListRequest;
import plus.xyc.server.i18n.entry.entity.response.EntryCountResponse;
import plus.xyc.server.i18n.entry.entity.response.EntryResponse;
import plus.xyc.server.i18n.entry.entity.response.EntryWithResultResponse;
import plus.xyc.server.i18n.entry.mapper.EntryMapper;
import plus.xyc.server.i18n.entry.mapper.EntryResultMapper;
import plus.xyc.server.i18n.entry.service.EntryResultService;
import plus.xyc.server.i18n.entry.service.EntryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 词条 服务实现类
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
@Service
public class EntryServiceImpl extends ServiceImpl<EntryMapper, Entry> implements EntryService {

    @Resource
    private RedisTemplate<String, Integer> redisTemplate;
    @Resource
    private EntryResultService entryResultService;
    @Resource
    private EntryMapStruct entryMapStruct;
    @Resource
    private EntryResultMapper entryResultMapper;

    @Override
    public int wordCount(Long moduleId) {
        Integer count = redisTemplate.opsForValue().get("entry:word:count" + moduleId);
        if(count == null) {
            return updateWordCount(moduleId);
        }
        return count;
    }

    private int updateWordCount(Long moduleId) {
        List<Entry> entries = baseMapper.findByModuleId(moduleId);
        int count = entries.stream().mapToInt(entry -> entry.getValue().length()).sum();
        redisTemplate.opsForValue().set("entry:word:count" + moduleId, count, 1, TimeUnit.DAYS);
        return count;
    }

    @Override
    public PageResult<EntryResponse> query(PageQueryRequest query, EntryListRequest request) {
        Page<Entry> page = query.toPage();
        List<Entry> all = baseMapper.query(page, query.getKeyword(), request);
        List<Long> resultIds = all.stream().map(Entry::getResultId).toList();
        List<EntryResult> results = entryResultService.getResults(resultIds, request.getLanguage());
        List<EntryResponse> list = all.stream().map(item -> {
            EntryResult result = results.stream().filter(entryResult -> entryResult.getId().equals(item.getResultId())).findFirst().orElse(null);
            EntryResponse response = entryMapStruct.toEntryResponse(item);
            response.setTranslation(result);
            return response;
        }).toList();
        return PageResult.of(page.getTotal(), list);
    }

    @Override
    public EntryCountResponse count(EntryCountRequest request) {
        EntryCountResponse response = new EntryCountResponse();
        response.setTotal(baseMapper.countTotal(request));
        response.setTranslated(baseMapper.countTranslated(request));
        response.setVerified(baseMapper.countVerified(request));
        return response;
    }

    @Override
    public List<EntryWithResultResponse> getEntryByBranchIdWithResult(Long branchId) {
        List<Entry> entries = baseMapper.findByBranchId(branchId);
        List<Long> entryIds = entries.stream().map(Entry::getId).toList();
        List<EntryResult> results = entryResultMapper.findByEntryIds(entryIds);
        return entries.stream().map(entry -> {
            List<EntryResult> entryResults = results.stream().filter(entryResult -> entryResult.getEntryId().equals(entry.getId())).toList();
            EntryWithResultResponse response = entryMapStruct.toEntryWithResultResponse(entry);
            response.setResults(entryResults);
            return response;
        }).toList();
    }

    @Override
    public void cloneEntriesBySourceId(Long sourceId, Long targetId) {
        List<EntryWithResultResponse> entries = getEntryByBranchIdWithResult(sourceId);
        cloneEntries(entries, targetId);
    }

    @Override
    public void cloneEntries(List<EntryWithResultResponse> sources, Long targetId) {
        sources.forEach(entry -> {
            entry.setId(null);
            entry.setBranchId(targetId);
            entry.setUpdateTime(new Date());
        });
        List<Entry> all = sources.stream().map(entry -> entryMapStruct.toEntryFromEntryWithResultResponse(entry)).toList();
        // 保存数据
        // 每次插入100条
        for (int i = 0; i < sources.size(); i += 100) {
            List<Entry> part = all.subList(i, Math.min(i + 100, sources.size()));
            EntryService self = (EntryService)AopContext.currentProxy();
            self.saveBatch(part);
        }
        sources.forEach(entry -> {
            entry.setId(Objects.requireNonNull(all.stream().filter(item -> item.getValue().equals(entry.getValue())).findFirst().orElse(null)).getId());
        });

        List<EntryResult> results = sources.stream().map(entry -> entry.getResults().stream().peek(result -> {
            result.setId(null);
            result.setEntryId(entry.getId());
            result.setUpdateTime(new Date());
        }).toList()).toList().stream().flatMap(List::stream).toList();
        // 保存数据
        // 每次插入100条
        for (int i = 0; i < results.size(); i += 100) {
            List<EntryResult> part = results.subList(i, Math.min(i + 100, results.size()));
            entryResultService.saveBatch(part);
        }
    }
}
