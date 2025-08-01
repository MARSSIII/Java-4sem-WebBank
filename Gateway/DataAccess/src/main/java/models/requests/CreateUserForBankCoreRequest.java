package models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import models.DTO.users.enums.HairColor;
import models.DTO.users.enums.Sex;

@Data
@AllArgsConstructor
public class CreateUserForBankCoreRequest {
    private String userName;

    private String login;

    private Integer yearBirth;

    private Sex gender;

    private HairColor hairColor;
}
