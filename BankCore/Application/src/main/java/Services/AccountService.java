package Services;

import contracts.AccountServiceInterface;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import Data.models.transactions.Transaction;
import Data.models.transactions.enums.TransactionState;
import Data.models.transactions.enums.TransactionType;
import Data.models.users.User;
import org.springframework.stereotype.Service;
import Data.models.accounts.Account;
import Data.repositories.AccountRepositoryJPA;
import Data.repositories.TransactionRepositoryJPA;
import Data.repositories.UserRepositoryJPA;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Сервис для управления счетами (аккаунтами).
 * Предоставляет методы для создания, входа, просмотра баланса, истории операций,
 * снятия, пополнения и перевода средств между аккаунтами.
 */
@Service
@AllArgsConstructor
public class AccountService implements AccountServiceInterface {

    final AccountRepositoryJPA accountRepository;

    final UserRepositoryJPA userRepositoryJPA;

    final TransactionRepositoryJPA transactionRepository;

    /**
     * Создает новый аккаунт для указанного пользователя.
     *
     * @param userId идентификатор владельца аккаунта.
     * @return Созданный аккаунт.
     * @throws Exception Если логин владельца пуст.
     */
    @Override
    @Transactional
    public Account CreateAccount(UUID userId) {
        User user = userRepositoryJPA.findById(userId)
                .orElse(null);

        if (user == null){
            throw new IllegalArgumentException("user not found");
        }

        Account account = Account.builder().ownerId(userId).build();
        accountRepository.save(account);

        return account;
    }

    /**
     * Возвращает {@link Account} по его уникальному идентификатору.
     *
     * @param accountId уникальный идентификатор аккаунта, который требуется получить
     * @return {@link Account} с указанным идентификатором
     * @throws IllegalArgumentException если аккаунт с данным идентификатором не найден
     */
    @Override
    public Account GetAccountById(UUID accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }

    /**
     * Возвращает список всех аккаунтов для указанного логина владельца.
     *
     * @param userId Логин владельца аккаунтов.
     * @return Список аккаунтов. Если логин пуст, возвращается пустой список.
     * @throws IllegalArgumentException если передан {@code userId = null}, то кидается исключения, что не указан userId
     */
    @Override
    public List<Account> GetAllAccountsForOwner(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId is null");
        }

        return accountRepository.findByOwnerId(userId);
    }

    @Override
    public List<Account> GetAllAccounts(){
        return accountRepository.findAll();
    }

    /**
     * Отображает баланс указанного аккаунта.
     *
     * @param accountId идентификатор счёта, для которого требуется вернуть информацию о балансе.
     * @return string с показателями баланса и идентификатора для счёта
     */
    @Override
    public String ShowBalance(UUID accountId) {
        if (accountId == null) {
            throw new IllegalArgumentException("Account id is required");
        }

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        Double balance = account.getBalance();
        return String.format("Balance for account %d: %.2f", accountId, balance);
    }


    /**
     * Снимает указанную сумму с баланса аккаунта.
     *
     * @param account Аккаунт, с которого необходимо снять средства.
     * @param amount Сумма для снятия.
     * @throws IllegalArgumentException Если аккаунт равен {@code null} или недостаточно средств на балансе.
     */
    @Override
    @Transactional
    public void WithdrawBalance(Account account, Double amount) {
        if (account == null) {
            throw new IllegalArgumentException("Account id is required");
        }
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (account.getBalance() < amount) {
            throw new IllegalArgumentException("Not enough Money");
        }
        account.changeBalance(-amount);
        accountRepository.save(account);
        Transaction transaction = Transaction.builder()
                .account(account)
                .transactionType(TransactionType.WITHDRAW)
                .transactionState(TransactionState.COMMIT)
                .amount(amount)
                .timestamp(LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);
    }

    /**
     * Пополняет баланс указанного аккаунта на указанную сумму.
     *
     * @param account Аккаунт, который необходимо пополнить.
     * @param amount Сумма для пополнения.
     * @throws Exception Если аккаунт равен {@code null}.
     */
    @Override
    @Transactional
    public void RefillBalance(Account account, Double amount) {
        if (account == null) {
            throw new IllegalArgumentException("Account id is required");
        }
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        account.changeBalance(amount);
        accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .account(account)
                .transactionType(TransactionType.REFILL)
                .transactionState(TransactionState.COMMIT)
                .amount(amount)
                .timestamp(LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);
    }

    /**
     * Переводит средства с одного аккаунта на другой с учетом процента.
     *
     * @param account Аккаунт, с которого переводятся средства.
     * @param otherAccount Аккаунт, на который переводятся средства.
     * @param amount Сумма для перевода.
     * @param percent Процент, который будет начислен на переводимую сумму.
     * @throws Exception Если один из аккаунтов равен {@code null}.
     * @throws IllegalArgumentException Если сумма меньше или равна нулю.
     */
    @Override
    @Transactional
    public void ForwardingBalance(Account account, Account otherAccount, Double amount, Double percent) throws Exception {
        if (account == null || otherAccount == null) {
            throw new Exception("AccountId is required");
        }
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Amount must be a positive number");
        }
        if (percent == null || percent <= 0) {
            throw new IllegalArgumentException("Percent must be positive");
        }
        if (account.getBalance() < amount) {
            throw new IllegalArgumentException("Not enough Money");
        }

        double amountWithPercent = amount * percent;

        account.changeBalance(-amount);
        otherAccount.changeBalance(amountWithPercent);
        accountRepository.save(account);
        accountRepository.save(otherAccount);

        Transaction transactionFrom = Transaction.builder()
                .account(account)
                .transactionType(TransactionType.TRANSFER)
                .transactionState(TransactionState.COMMIT)
                .amount(-amount)
                .timestamp(LocalDateTime.now())
                .build();
        transactionRepository.save(transactionFrom);

        Transaction transactionTo = Transaction.builder()
                .account(otherAccount)
                .transactionType(TransactionType.TRANSFER)
                .transactionState(TransactionState.COMMIT)
                .amount(amountWithPercent)
                .timestamp(LocalDateTime.now())
                .build();
        transactionRepository.save(transactionTo);
    }
}
