package Services;

import contracts.TransactionServiceInterface;
import lombok.AllArgsConstructor;
import Data.models.transactions.Transaction;
import Data.models.transactions.enums.TransactionType;
import org.springframework.stereotype.Service;
import Data.repositories.TransactionRepositoryJPA;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TransactionService implements TransactionServiceInterface {

    final TransactionRepositoryJPA transactionRepository;

    @Override
    public List<Transaction> GetListTransaction(UUID accountId, TransactionType txType) {
        if (accountId == null) {
            throw new IllegalArgumentException("Account id must not be null");
        }

        if (txType == null) {
            return transactionRepository.findByAccountId(accountId);
        } else {
            return transactionRepository.findByAccountIdAndTransactionType(accountId, txType);
        }
    }
}
