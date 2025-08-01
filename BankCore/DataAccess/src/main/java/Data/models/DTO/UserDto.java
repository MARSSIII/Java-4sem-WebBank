package Data.models.DTO;

import lombok.Data;
import Data.models.users.User;
import Data.models.users.enums.HairColor;
import Data.models.users.enums.Sex;

import java.util.UUID;

/**
 * DTO (Data Transfer Object) для передачи информации о пользователе между слоями приложения.
 * Используется, чтобы безопасно получать и отправлять данные пользователя через REST API без раскрытия всех полей сущности.
 */
@Data
public class UserDto {

    /**
     * Идентификатор пользователя
     */
    private UUID Id;

    /**
     * Имя пользователя.
     */
    private String name;

    private String login;

    /**
     * Год рождения пользователя.
     */
    private Integer yearBirth;

    /**
     * Пол пользователя (например, "MALE", "FEMALE").
     */
    private Sex gender;

    /**
     * Цвет волос пользователя (например, "BROWN", "BLONDE", "BLACK" и т.д.).
     */
    private HairColor hairColor;

    public static UserDto from(User user) {
        if (user == null){
            return null;
        }

        UserDto dto = new UserDto();

        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setLogin(user.getLogin());
        dto.setGender(user.getGender());
        dto.setHairColor(user.getHairColor());
        dto.setYearBirth(user.getYearBirth());

        return dto;
    }
}