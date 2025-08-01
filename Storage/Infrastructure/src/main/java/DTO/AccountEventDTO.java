package DTO;

import Data.models.AccountEvent;
import lombok.Data;

import java.util.UUID;

@Data
public class AccountEventDTO {
    private UUID eventId;

    private UUID accountId;

    private String eventType;

    private String ownerLogin;

    private double amount;

    private double balance;

    private UUID forwardId;
}
