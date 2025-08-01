package Data.events.eventDTO;

import Data.events.eventModels.AccountEvent;
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

    public static AccountEventDTO from(AccountEvent accountEvent) {
        if (accountEvent == null){
            return null;
        }

        AccountEventDTO dto = new AccountEventDTO();

        dto.setEventId(accountEvent.getEventId());
        dto.setAccountId(accountEvent.getAccountId());
        dto.setEventType(accountEvent.getEventType());
        dto.setOwnerLogin(accountEvent.getOwnerLogin());
        dto.setAmount(accountEvent.getAmount());
        dto.setBalance(accountEvent.getBalance());
        dto.setForwardId(accountEvent.getForwardId());

        return dto;
    }
}
