package Data.repositories;

import Data.models.transactions.Transaction;
import Data.models.transactions.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepositoryJPA extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByAccountIdAndTransactionType(UUID accountId, TransactionType transactionType);

    List<Transaction> findByAccountId(UUID accountId);
}
