package Kafka;

import Services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class EventConsumer {
    private final EventService eventService;

    @KafkaListener(topics = "client-topic", groupId = "storage-group")
    public void consumeUser(String message) {
        eventService.processUserEvent(message);
    }

    @KafkaListener(topics = "account-topic", groupId = "storage-group")
    public void consumeAccount(String message) {
        eventService.processAccountEvent(message);
    }
}
