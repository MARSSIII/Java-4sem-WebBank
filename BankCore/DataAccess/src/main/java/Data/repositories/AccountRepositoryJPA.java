package Data.repositories;

import Data.models.accounts.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepositoryJPA extends JpaRepository<Account, UUID> {

    /**
     * Найти список аккаунтов по логину владельца.
     */
    List<Account> findByOwnerId(UUID ownerId);

    /**
     * Загрузить аккаунт с транзакциями по id аккаунта.
     * Используется fetch join для избежания lazy loading проблем.
     */
    @Query("SELECT a FROM Account a LEFT JOIN FETCH a.transactions WHERE a.id = :accountId")
    Optional<Account> findAccountWithTransactionsById(@Param("accountId") UUID accountId);
}
