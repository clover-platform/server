package plus.xyc.server.wiki.ai.service;

import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.zkit.support.server.ai.api.entity.InvokeRequest;
import org.zkit.support.server.ai.api.service.AIAPIService;

import java.util.List;

@Service
public class AIService {

    @Resource
    private AIAPIService aiapiService;

    public SseEmitter chat(String content) {
        InvokeRequest request = new InvokeRequest();
        request.setContent(content);
        JSONObject filter = new JSONObject();
        filter.put("source", "wiki");
        request.setFilter(filter);
        request.setMessages(List.of());
        return aiapiService.stream(request);
    }

}
