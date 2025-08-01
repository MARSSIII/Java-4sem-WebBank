package models.DTO.users;

import lombok.Data;
import models.DTO.users.enums.HairColor;
import models.DTO.users.enums.Sex;

import java.util.UUID;

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

}