package contracts;

import Data.models.accounts.Account;

import java.util.List;
import java.util.UUID;

/**
 * Интерфейс сервиса для управления учетными записями (аккаунтами).
 * Определяет методы для создания, входа, просмотра баланса, истории операций,
 * снятия, пополнения и перевода средств между аккаунтами.
 */
public interface AccountServiceInterface {

    /**
     * Создает новый аккаунт для указанного пользователя.
     *
     * @param ownerLogin Логин владельца аккаунта.
     * @return Созданный аккаунт.
     * @throws Exception Если логин владельца пуст.
     */
    Account CreateAccount(UUID ownerId) throws Exception;

    /**
     * Получения сущности Account по идентификатору
     * @params accountId, идентификатор принадлежащий сущности account
     * @return account, сущность account соотв. переданому идентификатору или {@code null} если account не найден
     */
    Account GetAccountById(UUID accountId);

    /**
     * Возвращает список всех аккаунтов для указанного логина владельца.
     *
     * @param ownerId Логин владельца аккаунтов.
     * @return Список аккаунтов. Если логин пуст, возвращается пустой список.
     */
    List<Account> GetAllAccountsForOwner(UUID ownerId);

    List<Account> GetAllAccounts();

    /**
     * Отображает баланс указанного аккаунта.
     *
     * @param accountId идентификатор счёта, для которого требуется вернуть информацию о балансе.
     * @return string содержит информация о балансе и идентификаторе счёта
     */
    String ShowBalance(UUID accountId);

    /**
     * Отображает историю операций для указанного аккаунта.
     *
     * @param account Аккаунт, для которого необходимо отобразить историю операций.
     */
    // void ShowOperationHistory(Account account, TransactionType type);

    /**
     * Снимает указанную сумму с баланса аккаунта.
     *
     * @param account Аккаунт, с которого необходимо снять средства.
     * @param amount Сумма для снятия.
     * @throws Exception Если аккаунт равен {@code null} или недостаточно средств на балансе.
     */
    void WithdrawBalance(Account account, Double amount) throws Exception;

    /**
     * Пополняет баланс указанного аккаунта на указанную сумму.
     *
     * @param account Аккаунт, который необходимо пополнить.
     * @param amount Сумма для пополнения.
     * @throws Exception Если аккаунт равен {@code null}.
     */
    void RefillBalance(Account account, Double amount) throws Exception;

    /**
     * Переводит средства с одного аккаунта на другой с учетом процента.
     *
     * @param account Аккаунт, с которого переводятся средства.
     * @param idOtherAccount Аккаунт, на который переводятся средства.
     * @param amount Сумма для перевода.
     * @param percent Процент, который будет начислен на переводимую сумму.
     * @throws Exception Если один из аккаунтов равен {@code null}.
     * @throws IllegalArgumentException Если сумма равна нулю.
     */
    void ForwardingBalance(Account account, Account idOtherAccount, Double amount, Double percent) throws Exception;
}
