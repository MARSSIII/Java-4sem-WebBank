package Data.models.DTO;

import lombok.Data;
import Data.models.transactions.Transaction;
import Data.models.transactions.enums.TransactionState;
import Data.models.transactions.enums.TransactionType;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO для объекта Transaction.
 * Используется для передачи данных о транзакции через REST.
 */
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


    public static TransactionDto from(Transaction transaction) {
        if (transaction == null){
            return null;
        }

        TransactionDto dto = new TransactionDto();

        dto.setAccountId(transaction.getAccount().getId());
        dto.setAmount(transaction.getAmount());
        dto.setTimestamp(transaction.getTimestamp());
        dto.setTransactionState(transaction.getTransactionState());
        dto.setTransactionType(transaction.getTransactionType());

        return dto;
    }
}