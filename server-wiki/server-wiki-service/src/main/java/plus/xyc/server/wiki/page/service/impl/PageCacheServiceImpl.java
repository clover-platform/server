package plus.xyc.server.wiki.page.service.impl;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import plus.xyc.server.wiki.page.service.PageCacheService;

import java.util.Set;

@Service
public class PageCacheServiceImpl implements PageCacheService {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void clearCache(Long id) {
        String key = "wiki:book:page:" + id;
        redisTemplate.delete(key);
        Set<String> keys = redisTemplate.keys(key + "*");
        if (!ObjectUtils.isEmpty(keys)) {
            redisTemplate.delete(keys);
        }
    }
}
