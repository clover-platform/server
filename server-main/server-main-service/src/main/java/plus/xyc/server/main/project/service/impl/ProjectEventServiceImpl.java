package plus.xyc.server.main.project.service.impl;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSON;

import jakarta.annotation.Resource;
import plus.xyc.server.main.api.entity.event.ProjectEvent;
import plus.xyc.server.main.configuration.EventConfiguration;
import plus.xyc.server.main.project.service.ProjectEventService;

@Service
public class ProjectEventServiceImpl implements ProjectEventService {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Resource
    private EventConfiguration configuration;

    public void send(ProjectEvent event) {
        kafkaTemplate.send(configuration.getProjectTopic(), JSON.toJSONString(event));
    }

    @Override
    public void join(Long userId, Long projectId, Long teamId) {
        ProjectEvent event = new ProjectEvent()
            .setEventType(ProjectEvent.EVENT_TYPE_JOIN)
            .setUserId(userId)
            .setProjectId(projectId)
            .setTeamId(teamId);
        send(event);
    }

    @Override
    public void leave(Long userId, Long projectId, Long teamId) {
        ProjectEvent event = new ProjectEvent()
            .setEventType(ProjectEvent.EVENT_TYPE_LEAVE)
            .setUserId(userId)
            .setProjectId(projectId)
            .setTeamId(teamId);
        send(event);
    }

    @Override
    public void update(Long userId, Long projectId, Long teamId) {
        ProjectEvent event = new ProjectEvent()
            .setEventType(ProjectEvent.EVENT_TYPE_UPDATE)
            .setUserId(userId)
            .setProjectId(projectId)
            .setTeamId(teamId);
        send(event);
    }

    @Override
    public void delete(Long userId, Long projectId, Long teamId) {
        ProjectEvent event = new ProjectEvent()
            .setEventType(ProjectEvent.EVENT_TYPE_DELETE)
            .setUserId(userId)
            .setProjectId(projectId)
            .setTeamId(teamId);
        send(event);
    }

    @Override
    public void create(Long userId, Long projectId, Long teamId) {
        ProjectEvent event = new ProjectEvent()
            .setEventType(ProjectEvent.EVENT_TYPE_CREATE)
            .setUserId(userId)
            .setProjectId(projectId)
            .setTeamId(teamId);
        send(event);
    }

    
}
