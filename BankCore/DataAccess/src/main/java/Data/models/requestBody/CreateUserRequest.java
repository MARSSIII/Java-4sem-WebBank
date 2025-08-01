package Data.models.requestBody;

import lombok.Data;
import Data.models.users.enums.HairColor;
import Data.models.users.enums.Sex;

@Data
public class CreateUserRequest {
    private String userName;

    private String login;

    private Integer yearBirth;

    private Sex gender;

    private HairColor hairColor;
}
