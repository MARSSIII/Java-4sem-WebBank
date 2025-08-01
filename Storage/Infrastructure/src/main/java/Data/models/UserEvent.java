package Data.models;

import DTO.UserEventDTO;
import Data.models.enums.HairColor;
import Data.models.enums.Sex;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "user_event")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEvent {

    @Id
    @Column(name = "event_id")
    private UUID eventId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column
    private String name;

    @Column
    private Integer yearBirth;

    @Column
    private Sex gender;

    @Column
    private HairColor hairColor;

    @ElementCollection
    @CollectionTable(name = "user_event_friends", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "friend_userId")
    private Set<UUID> friends;

    public static UserEvent from(UserEventDTO userEventDTO) {
        if (userEventDTO == null){
            return null;
        }

        return UserEvent.builder()
                .eventId(userEventDTO.getEventId())
                .eventType(userEventDTO.getEventType())
                .userId(userEventDTO.getUserId())
                .yearBirth(userEventDTO.getYearBirth())
                .name(userEventDTO.getName())
                .hairColor(userEventDTO.getHairColor())
                .gender(userEventDTO.getGender())
                .friends(userEventDTO.getFriends())
                .build();
    }
}
