package plus.xyc.server.wiki.ai.service;

import jakarta.annotation.Resource;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.zkit.support.server.ai.api.entity.InvokeRequest;
import org.zkit.support.server.ai.api.service.ChatApiService;

import plus.xyc.server.wiki.ai.entity.AIChatRequest;
import plus.xyc.server.wiki.configuration.AppConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AIService {

    @DubboReference
    private ChatApiService chatApiService;
    @Resource
    private AppConfiguration configuration;

    public String chat(AIChatRequest req) {
        InvokeRequest request = new InvokeRequest();
        request.setMessage(req.getContent());
        if("chat".equals(req.getType())) {
            Map<String, String> filter = new HashMap<>();
            filter.put("source", "wiki");
            request.setMetadata(filter);
            request.setRules(configuration.getChatRules());
            request.setUseContext(true);
        } else {
            List<String> list = new ArrayList<>(configuration.getWriterRules());
            list.add("已完成的编写：" + req.getData());
            request.setRules(list);
            request.setUseContext(false);
        }
        return chatApiService.stream(request);
    }

}
