package contracts;

import Data.models.transactions.Transaction;
import Data.models.transactions.enums.TransactionType;

import java.util.List;
import java.util.UUID;

public interface TransactionServiceInterface {
    List<Transaction> GetListTransaction(UUID accountId, TransactionType txType) throws Exception;
}
