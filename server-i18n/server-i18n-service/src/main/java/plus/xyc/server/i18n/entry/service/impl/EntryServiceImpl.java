package plus.xyc.server.i18n.entry.service.impl;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import plus.xyc.server.i18n.entry.entity.dto.Entry;
import plus.xyc.server.i18n.entry.mapper.EntryMapper;
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
}
