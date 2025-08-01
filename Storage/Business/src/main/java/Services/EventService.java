package Services;

import DTO.AccountEventDTO;
import DTO.UserEventDTO;
import Data.models.AccountEvent;
import Data.models.UserEvent;
import Data.repositories.AccountEventRepositoryJPA;
import Data.repositories.UserEventRepositoryJPA;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {
    private final UserEventRepositoryJPA userEventRepository;

    private final AccountEventRepositoryJPA accountEventRepository;

    private final ObjectMapper objectMapper;

    public void processUserEvent(String message){
        try{
            UserEventDTO dto = objectMapper.readValue(message, UserEventDTO.class);

            UserEvent userEvent = UserEvent.from(dto);
            userEventRepository.save(userEvent);

        } catch (Exception exc){
            throw new RuntimeException("Failed to parse user event", exc);
        }
    }

    public void processAccountEvent(String message){
        try {
            AccountEventDTO dto = objectMapper.readValue(message, AccountEventDTO.class);

            AccountEvent accountEvent = AccountEvent.from(dto);
            accountEventRepository.save(accountEvent);
        } catch (Exception exc) {
            throw new RuntimeException("Failed to parse account event", exc);
        }
    }
}
