package models.DTO.transactions;

import lombok.Data;
import models.DTO.transactions.enums.TransactionState;
import models.DTO.transactions.enums.TransactionType;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TransactionDto {

    /**
     * Идентификатор счета, к которому относится транзакция.
     */
    private UUID accountId;

    /**
     * Тип транзакции (например, DEPOSIT, WITHDRAWAL).
     */
    private TransactionType transactionType;

    /**
     * Состояние транзакции (например, COMPLETED, PENDING).
     */
    private TransactionState transactionState;

    /**
     * Сумма транзакции.
     */
    private Double amount;

    /**
     * Время совершения транзакции, например, "2023-06-23T12:00:00".
     */
    private LocalDateTime timestamp;

}