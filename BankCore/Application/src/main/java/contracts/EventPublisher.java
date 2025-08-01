package contracts;

import Data.events.eventModels.AccountEvent;
import Data.events.eventModels.UserEvent;

public interface EventPublisher {
    void publishUserEvent(UserEvent userEvent);

    void publishAccountEvent(AccountEvent accountEvent);
}
