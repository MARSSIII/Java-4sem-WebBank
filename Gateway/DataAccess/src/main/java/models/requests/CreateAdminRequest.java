package models.requests;

import lombok.Data;

@Data
public class CreateAdminRequest {
    private String password;

    private String login;
}
