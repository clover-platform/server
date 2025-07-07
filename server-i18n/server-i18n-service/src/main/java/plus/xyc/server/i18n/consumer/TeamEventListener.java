package plus.xyc.server.i18n.consumer;

import lombok.extern.slf4j.Slf4j;
import plus.xyc.server.main.api.entity.event.TeamEvent;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSON;

import java.util.List;

@Component
@Slf4j
public class TeamEventListener {

    @KafkaListener(topics = {"${event.team-topic}"}, containerFactory = "batchFactory")
    public void batchConsumer(List<ConsumerRecord<String, String>> consumerRecords) {
        consumerRecords.forEach(record -> {
            TeamEvent event = JSON.parseObject(record.value(), TeamEvent.class);
            log.info("TeamEventListener: {}", event);
        });
    }

}
