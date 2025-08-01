package DTO;

import Data.models.UserEvent;
import Data.models.enums.HairColor;
import Data.models.enums.Sex;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class UserEventDTO {
    private UUID eventId;

    private UUID userId;

    private String eventType;

    private String name;

    private Integer yearBirth;

    private Sex gender;

    private HairColor hairColor;

    private Set<UUID> friends;
}
