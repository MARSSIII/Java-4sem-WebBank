package Data.models;

import DTO.AccountEventDTO;
import DTO.UserEventDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "account_event")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountEvent {

    @Id
    @Column(name = "id")
    private UUID eventId;

    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "owner_login")
    private String ownerLogin;

    @Column(name = "amount")
    private double amount;

    @Column(name = "balance")
    private double balance;

    @Column(name = "forwardId")
    private UUID forwardId;

    public static AccountEvent from(AccountEventDTO accountEventDTO) {
        if (accountEventDTO == null){
            return null;
        }

        return AccountEvent
                .builder()
                .eventId(accountEventDTO.getEventId())
                .accountId(accountEventDTO.getAccountId())
                .eventType(accountEventDTO.getEventType())
                .ownerLogin(accountEventDTO.getOwnerLogin())
                .amount(accountEventDTO.getAmount())
                .balance(accountEventDTO.getBalance())
                .forwardId(accountEventDTO.getForwardId())
                .build();
    }
}
