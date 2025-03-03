package plus.xyc.server.i18n.language.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import plus.xyc.server.i18n.language.entity.dto.Language;
import plus.xyc.server.i18n.language.entity.response.LanguageResponse;
import plus.xyc.server.i18n.language.service.LanguageService;

import java.util.List;

/**
 * <p>
 * 语言 前端控制器
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/language")
@Tag(name = "LanguageController", description = "语言")
public class LanguageController {

    @Resource
    private LanguageService languageService;

    @GetMapping("/list")
    @Operation(summary = "语言列表")
    public List<LanguageResponse> list() {
        return languageService.all();
    }

}
