package plus.xyc.server.i18n.file.service.impl;

import plus.xyc.server.i18n.entry.entity.dto.Entry;
import plus.xyc.server.i18n.entry.service.EntryService;
import plus.xyc.server.i18n.file.entity.dto.FileRevision;
import plus.xyc.server.i18n.file.mapper.FileRevisionMapper;
import plus.xyc.server.i18n.file.service.FileRevisionCommitService;
import plus.xyc.server.i18n.file.service.FileRevisionService;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.zkit.support.starter.boot.utils.MessageUtils;

/**
 * <p>
 * 变更 服务实现类
 * </p>
 *
 * @author generator
 * @since 2025-06-17
 */
@Service
public class FileRevisionServiceImpl extends ServiceImpl<FileRevisionMapper, FileRevision> implements FileRevisionService {

    @Resource
    private EntryService entryService;
    @Resource
    private FileRevisionCommitService fileRevisionCommitService;

    @Override
    @Transactional
    public void init(Long fileId, Long userId, String url, List<Entry> entries) {
        if (entries.isEmpty()) {
            return;
        }

        Date now = new Date();
        entries.forEach(entry -> {
            entry.setFileId(fileId);
            entry.setCreateUserId(userId);
            entry.setUpdateUserId(userId);
        });
        entryService.saveBatch(entries);

        String message = MessageUtils.get("branch.revision.init.message");
        FileRevision revision = new FileRevision();
        revision.setFileId(fileId);
        revision.setCreateTime(now);
        revision.setCreateUser(userId);
        revision.setMessage(message);
        revision.setFileUrl(url);
        revision.setCurrent(true);
        revision.setAddedSize(entries.size());
        revision.setUpdatedSize(0);
        revision.setDeletedSize(0);
        save(revision);

        // 插入 commit 记录
        fileRevisionCommitService.add(revision.getId(), entries.stream().map(Entry::getId).toList());
    }

    @Override
    public List<FileRevision> findListByFileIds(List<Long> fileIds) {
        if(fileIds == null || fileIds.isEmpty()) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(new LambdaQueryWrapper<FileRevision>().in(FileRevision::getFileId, fileIds).eq(FileRevision::getCurrent, true));
    }

}
