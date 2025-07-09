package plus.xyc.server.i18n.consumer;

import lombok.extern.slf4j.Slf4j;
import plus.xyc.server.i18n.module.service.ModuleService;
import plus.xyc.server.main.api.entity.event.ProjectEvent;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSON;

import jakarta.annotation.Resource;

import java.util.List;

@Component
@Slf4j
public class ProjectEventListener {

    @Resource
    private ModuleService moduleService;

    @KafkaListener(topics = {"${event.project-topic}"}, containerFactory = "batchFactory")
    public void batchConsumer(List<ConsumerRecord<String, String>> consumerRecords) {
        consumerRecords.forEach(record -> {
            ProjectEvent event = JSON.parseObject(record.value(), ProjectEvent.class);
            log.info("ProjectEventListener: {}", event);
            switch (event.getEventType()) {
                case ProjectEvent.EVENT_TYPE_LEAVE:
                    moduleService.leave(event.getUserId(), event.getProjectId());
                    break;
            }
        });
    }

}
