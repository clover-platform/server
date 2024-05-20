package plus.xyc.server.i18n.entry.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zkit.support.starter.mybatis.entity.PageQueryRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import plus.xyc.server.i18n.branch.entity.request.AllBranchRequest;
import plus.xyc.server.i18n.branch.entity.response.BranchResponse;
import plus.xyc.server.i18n.entry.entity.request.EntryCountRequest;
import plus.xyc.server.i18n.entry.entity.request.EntryListRequest;
import plus.xyc.server.i18n.entry.entity.response.EntryCountResponse;
import plus.xyc.server.i18n.entry.entity.response.EntryResponse;
import plus.xyc.server.i18n.entry.service.EntryService;

import java.util.List;

/**
 * <p>
 * 词条 前端控制器
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/entry")
@Tag(name = "entry", description = "词条")
public class EntryController {

    @Resource
    private EntryService entryService;

    @GetMapping("/list")
    @Operation(summary = "查询词条")
    public PageResult<EntryResponse> list(
            @ParameterObject @ModelAttribute PageQueryRequest page,
            @ParameterObject @ModelAttribute EntryListRequest request
    ) {
        return entryService.query(page, request);
    }

    @GetMapping("/count")
    @Operation(summary = "查询词条")
    public EntryCountResponse count(@ParameterObject @ModelAttribute EntryCountRequest request) {
        return entryService.count(request);
    }

}
