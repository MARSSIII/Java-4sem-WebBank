package models.requests;

import lombok.Data;
import models.DTO.users.enums.HairColor;
import models.DTO.users.enums.Sex;

@Data
public class CreateUserRequest {
    private String userName;

    private String password;

    private String login;

    private Integer yearBirth;

    private Sex gender;

    private HairColor hairColor;
}
