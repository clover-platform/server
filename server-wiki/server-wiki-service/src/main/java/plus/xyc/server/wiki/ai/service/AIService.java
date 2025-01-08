package plus.xyc.server.wiki.ai.service;

import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.zkit.support.server.ai.api.entity.InvokeRequest;
import org.zkit.support.server.ai.api.entity.Message;
import org.zkit.support.server.ai.api.service.AIAPIService;
import plus.xyc.server.wiki.ai.entity.AIChatRequest;
import plus.xyc.server.wiki.configuration.AppConfiguration;

import java.util.ArrayList;
import java.util.List;

@Service
public class AIService {

    @Resource
    private AIAPIService aiapiService;
    @Resource
    private AppConfiguration configuration;

    public SseEmitter chat(AIChatRequest req) {
        InvokeRequest request = new InvokeRequest();
        request.setContent(req.getContent());
        if("chat".equals(req.getType())) {
            JSONObject filter = new JSONObject();
            filter.put("source", "wiki");
            request.setFilter(filter);
            request.setMessages(configuration.getChatPrompts());
            request.setUseVector(true);
        } else {
            List<Message> list = new ArrayList<>(configuration.getWriterPrompts());
            Message data = new Message();
            data.setRole("user");
            data.setContent("已完成的编写：" + req.getData());
            list.add(data);
            request.setMessages(list);
        }
        return aiapiService.stream(request);
    }

}
