package Data.events.eventDTO;

import Data.events.eventModels.AccountEvent;
import Data.events.eventModels.UserEvent;
import Data.models.users.User;
import Data.models.users.enums.HairColor;
import Data.models.users.enums.Sex;

import java.util.Set;
import java.util.UUID;

public class UserEventDTO {
    private UUID eventId;

    private UUID userId;

    private String eventType;

    private String name;

    private Integer yearBirth;

    private Sex gender;

    private HairColor hairColor;

    private Set<UUID> friends;

    public static UserEventDTO from(UserEvent userEvent) {
        if (userEvent == null){
            return null;
        }

        UserEventDTO userEventDTO = new UserEventDTO();

        userEventDTO.eventId = userEvent.getEventId();
        userEventDTO.userId = userEvent.getUserId();
        userEventDTO.eventType = userEvent.getEventType();
        userEventDTO.name = userEvent.getName();
        userEventDTO.yearBirth = userEvent.getYearBirth();
        userEventDTO.gender = userEvent.getGender();
        userEventDTO.hairColor = userEvent.getHairColor();
        userEventDTO.friends = userEvent.getFriends();

        return userEventDTO;
    }
}
