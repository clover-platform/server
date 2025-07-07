package plus.xyc.server.main.team.service.impl;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSON;

import plus.xyc.server.main.api.entity.event.TeamEvent;
import plus.xyc.server.main.configuration.EventConfiguration;
import plus.xyc.server.main.team.service.TeamEventService;

import jakarta.annotation.Resource;

@Service
public class TeamEventServiceImpl implements TeamEventService {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Resource
    private EventConfiguration configuration;

    public void send(TeamEvent event) {
        kafkaTemplate.send(configuration.getTeamTopic(), JSON.toJSONString(event));
    }

    @Override
    public void join(Long userId, Long teamId) {
        TeamEvent event = new TeamEvent()
            .setEventType(TeamEvent.EVENT_TYPE_JOIN)
            .setUserId(userId)
            .setTeamId(teamId);
        send(event);
    }

    @Override
    public void leave(Long userId, Long teamId) {
        TeamEvent event = new TeamEvent()
            .setEventType(TeamEvent.EVENT_TYPE_LEAVE)
            .setUserId(userId)
            .setTeamId(teamId);
        send(event);
    }

    @Override
    public void update(Long userId, Long teamId) {
        TeamEvent event = new TeamEvent()
            .setEventType(TeamEvent.EVENT_TYPE_UPDATE)
            .setUserId(userId)
            .setTeamId(teamId);
        send(event);
    }

    @Override
    public void delete(Long userId, Long teamId) {
        TeamEvent event = new TeamEvent()
            .setEventType(TeamEvent.EVENT_TYPE_DELETE)
            .setUserId(userId)
            .setTeamId(teamId);
        send(event);
    }

    @Override
    public void create(Long userId, Long teamId) {
        TeamEvent event = new TeamEvent()
            .setEventType(TeamEvent.EVENT_TYPE_CREATE)
            .setUserId(userId)
            .setTeamId(teamId);
        send(event);
    }
}

