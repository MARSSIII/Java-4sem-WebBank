package models.DTO.accounts;

import lombok.Data;

import java.util.UUID;

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
}