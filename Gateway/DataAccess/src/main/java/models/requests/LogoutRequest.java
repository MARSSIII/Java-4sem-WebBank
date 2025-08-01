package models.requests;

import lombok.Data;

@Data
public class LogoutRequest {
    private final String token;
}
