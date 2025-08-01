package Infrastructure.kafka;

import Data.events.eventDTO.AccountEventDTO;
import Data.events.eventDTO.UserEventDTO;
import Data.events.eventModels.AccountEvent;
import Data.events.eventModels.UserEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import contracts.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaPublisher implements EventPublisher {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String CLIENT_TOPIC = "client-topic";
    private static final String ACCOUNT_TOPIC = "account-topic";

    @Override
    public void publishUserEvent(UserEvent userEvent) {
        try{
            UserEventDTO dto = UserEventDTO.from(userEvent);
            String json = objectMapper.writeValueAsString(dto);

            kafkaTemplate.send(CLIENT_TOPIC, json);
        } catch (Exception exc){
            throw new RuntimeException("Failed to publish user event", exc);
        }
    }

    @Override
    public void publishAccountEvent(AccountEvent accountEvent) {
        try {
            AccountEventDTO dto = AccountEventDTO.from(accountEvent);
            String json = objectMapper.writeValueAsString(dto);

            kafkaTemplate.send(ACCOUNT_TOPIC, json);
        } catch (Exception exc){
            throw new RuntimeException("Failed to publish account event", exc);
        }
    }
}
