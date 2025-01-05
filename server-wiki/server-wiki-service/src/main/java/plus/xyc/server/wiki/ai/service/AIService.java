package plus.xyc.server.wiki.ai.service;

import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.zkit.support.server.ai.api.entity.InvokeRequest;
import org.zkit.support.server.ai.api.service.AIAPIService;
import org.zkit.support.starter.boot.entity.Result;
import org.zkit.support.starter.boot.exception.ResultException;

import java.util.List;

@Service
public class AIService {

    @Resource
    private AIAPIService aiapiService;

    public String chat(String content) {
        InvokeRequest request = new InvokeRequest();
        request.setContent(content);
        JSONObject filter = new JSONObject();
        filter.put("source", "wiki");
        request.setFilter(filter);
        request.setMessages(List.of());
        try {
            aiapiService.stream(request);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
//        Result<String> result = aiapiService.invoke(request);
//        if(!result.isSuccess()) {
//            throw ResultException.internal();
//        }
//        return result.getData();
        return "ok";
    }

}
