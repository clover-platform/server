package plus.xyc.server.i18n.entry.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.zkit.support.starter.boot.entity.Result;
import org.zkit.support.starter.boot.exception.ResultException;
import org.zkit.support.starter.mybatis.entity.PageQueryRequest;
import org.zkit.support.starter.mybatis.entity.PageResult;
import plus.xyc.server.i18n.entry.entity.dto.EntryResult;
import plus.xyc.server.i18n.entry.entity.mapstruct.EntryResultMapStruct;
import plus.xyc.server.i18n.entry.entity.request.EntryResultListRequest;
import plus.xyc.server.i18n.entry.entity.response.EntryResultResponse;
import plus.xyc.server.i18n.entry.mapper.EntryResultMapper;
import plus.xyc.server.i18n.entry.service.EntryResultService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import plus.xyc.server.main.api.entity.response.ApiAccountResponse;
import plus.xyc.server.main.api.rest.MainAccountRestApi;

import java.util.*;

/**
 * <p>
 * 翻译结果 服务实现类
 * </p>
 *
 * @author generator
 * @since 2024-05-13
 */
@Service
public class EntryResultServiceImpl extends ServiceImpl<EntryResultMapper, EntryResult> implements EntryResultService {

    @Resource
    private EntryResultMapStruct mapStruct;
    @Resource
    private MainAccountRestApi mainAccountRestApi;

    @Override
    public List<EntryResult> getLastResults(List<Long> ids, String language) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return baseMapper.getLastResults(ids, language);
    }

    @Override
    public List<EntryResult> getResults(List<Long> ids, String language) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return baseMapper.getResults(ids, language);
    }

    @Override
    public PageResult<EntryResultResponse> query(PageQueryRequest page, EntryResultListRequest request) {
        Page<EntryResult> pageResult = page.toPage();
        List<EntryResult> list = baseMapper.query(pageResult, request);

        List<Long> translatorIds = list.stream().map(EntryResult::getTranslatorId).filter(Objects::nonNull).toList();
        List<Long> verifierIds = list.stream().map(EntryResult::getCheckerId).filter(Objects::nonNull).toList();
        Set<Long> ids = new HashSet<>(translatorIds);
        ids.addAll(verifierIds);
        List<Long> uniqueIds = new ArrayList<>(ids);
        Result<PageResult<ApiAccountResponse>> r =  mainAccountRestApi.list(uniqueIds, uniqueIds.size(), 1);
        if(!r.isSuccess()) {
            throw ResultException.internal();
        }

        List<EntryResultResponse> responses = list.stream().map(result -> {
            EntryResultResponse response = mapStruct.toEntryResultResponse(result);
            response.setTranslator(r.getData().getData().stream().filter(user -> user.getId().equals(result.getTranslatorId())).findFirst().orElse(null));
            response.setVerifier(r.getData().getData().stream().filter(user -> user.getId().equals(result.getCheckerId())).findFirst().orElse(null));
            return response;
        }).toList();
        return PageResult.of(pageResult.getTotal(), responses);
    }
}
