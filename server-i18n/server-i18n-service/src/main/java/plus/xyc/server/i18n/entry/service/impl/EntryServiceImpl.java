package plus.xyc.server.i18n.entry.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
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
import plus.xyc.server.i18n.entry.mapper.EntryMapper;
import plus.xyc.server.i18n.entry.service.EntryResultService;
import plus.xyc.server.i18n.entry.service.EntryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
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
}
