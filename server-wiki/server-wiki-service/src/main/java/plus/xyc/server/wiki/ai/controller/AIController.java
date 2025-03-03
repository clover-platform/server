package plus.xyc.server.wiki.ai.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.zkit.support.starter.security.annotation.CurrentUser;
import org.zkit.support.starter.security.entity.SessionUser;
import plus.xyc.server.wiki.ai.entity.AIChatRequest;
import plus.xyc.server.wiki.ai.service.AIService;

@RestController
@RequestMapping("/ai")
@Tag(name = "AIController", description = "AI")
public class AIController {

    @Resource
    private AIService aiService;

    @PostMapping("/chat")
    @Operation(summary = "复制页面")
    public SseEmitter chat(
            @RequestBody AIChatRequest request,
            @CurrentUser @Parameter(hidden = true) SessionUser user
    ) {
        return aiService.chat(request);
    }

}
