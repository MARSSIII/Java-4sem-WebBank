package Data.models.DTO;

import lombok.Data;
import Data.models.accounts.Account;

import java.util.UUID;

/**
 * DTO (Data Transfer Object) для передачи информации о счетах (Account) между слоями приложения.
 * Используется для безопасного обмена данными счета через REST API, не раскрывая всех деталей сущности.
 */
@Data
public class AccountDto {
    /**
     * Уникальный идентификатор счета.
     * Может быть null при создании нового счета.
     */
    private UUID id;

    private UUID ownerId;

    /**
     * Баланс счета.
     */
    private Double balance;


    public static AccountDto from(Account account) {
        if (account == null){
            return null;
        }

        AccountDto dto = new AccountDto();

        dto.setOwnerId(account.getOwnerId());
        dto.setId(account.getId());
        dto.setBalance(account.getBalance());

        return dto;
    }
}