package Data.events.eventModels;

import Data.models.users.enums.HairColor;
import Data.models.users.enums.Sex;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class UserEvent {
    private UUID eventId;

    private UUID userId;

    private String eventType;

    private String name;

    private Integer yearBirth;

    private Sex gender;

    private HairColor hairColor;

    private Set<UUID> friends;
}
