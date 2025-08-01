package Controllers;

import contracts.AccountServiceInterface;
import contracts.UserServiceInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import Data.models.DTO.AccountDto;
import Data.models.accounts.Account;
import Data.models.users.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Accounts", description = "Операции со счетами")
@RequiredArgsConstructor
public class AccountControllers {

  private final AccountServiceInterface accountService;

  private final UserServiceInterface userService;

  @GetMapping("/{userId}/all")
  @Operation(summary = "Получения всех аккаунтов", description = "Получения списка всех аккаунтов для пользователя по Id")
  public ResponseEntity<List<AccountDto>> getAllAccountForUser(@PathVariable UUID userId) {
    List<Account> accounts = accountService.GetAllAccountsForOwner(userId);
    List<AccountDto> dtos = accounts
        .stream()
        .map(AccountDto::from)
        .toList();

    return ResponseEntity.ok(dtos);
  }

  @GetMapping("/all")
  @Operation(summary = "Получение всех аккаунтов", description = "Полуение всех аккаунтов имеющихся в системе")
  public ResponseEntity<List<AccountDto>> getAllAccounts(){
    List<Account> accounts = accountService.GetAllAccounts();
    List<AccountDto> dtos = accounts
            .stream()
            .map(AccountDto::from)
            .toList();

    return ResponseEntity.ok(dtos);
  }

  @GetMapping("/{accountId}")
  @Operation(summary = "Получение счёта по идентификатору", description = "Получение данных о счёте по указанном accountId")
  public ResponseEntity<AccountDto> getAccountByAccountId(@PathVariable UUID accountId){
    Account account = accountService.GetAccountById(accountId);
    AccountDto accountDto = AccountDto
            .from(account);

    return ResponseEntity.ok(accountDto);
  }


  @PostMapping("/{userId}/create")
  @Operation(summary = "Создания счёта для пользователя", description = "Создание счета для пользователя указанного userid")
  public ResponseEntity<AccountDto> createAccountForUser(@PathVariable UUID userId) throws Exception {
    Account account = accountService.CreateAccount(userId);
    AccountDto accountDto = AccountDto
        .from(account);

    return ResponseEntity.ok(accountDto);
  }


  @GetMapping("/{accountId}/balance")
  @Operation(summary = "Просмотр баланса счёта", description = "Просмотр баланса счёта для указанного accountId")
  public ResponseEntity<String> showBalanceForAccount(@PathVariable UUID accountId) {
    String balanceInfo = accountService.ShowBalance(accountId);
    return ResponseEntity.ok(balanceInfo);
  }

  @PostMapping("/{accountId}/withdraw")
  @Operation(summary = "Снятие денег со счёта", description = "Снятие указанной суммы со счёта, идентификатор которого соответствует указаному accountId")
  public ResponseEntity<Void> withdrawAmountForAccount(@PathVariable UUID accountId, @RequestParam Double amount)
      throws Exception {
    Account account = accountService.GetAccountById(accountId);
    accountService.WithdrawBalance(account, amount);

    return ResponseEntity.ok().build();
  }

  @PostMapping("/{accountId}/refill")
  @Operation(summary = "Пополнение счёта", description = "Пополнение на указанную сумму счёта, идентификатор которого соотвествует указанному accountId")
  public ResponseEntity<Void> refillAmountForAccount(@PathVariable UUID accountId, @RequestParam Double amount)
      throws Exception {
    Account account = accountService.GetAccountById(accountId);
    accountService.RefillBalance(account, amount);

    return ResponseEntity.ok().build();
  }

  @PostMapping("/{accountId}/forward")
  @Operation(summary = "Перевод денег между счетами", description = "Перевод денег с одного счета на другой с комиссией")
  public ResponseEntity<Void> forwardingFromAccountToOtherAccount(
      @PathVariable UUID accountId,
      @RequestParam UUID otherAccountId,
      @RequestParam Double amount) throws Exception {
    Account account = accountService.GetAccountById(accountId);
    Account otherAccount = accountService.GetAccountById(otherAccountId);

    Double percent;
    User user = userService.GetUserById(account.getOwnerId());
    User otherUser = userService.GetUserById(otherAccount.getOwnerId());

    if (userService.IsFriendsUsers(user, otherUser.getLogin())) {
      percent = 0.97;
    } else if (user.equals(otherUser)) {
      percent = 1.0;
    } else {
      percent = 0.9;
    }

    accountService.ForwardingBalance(account, otherAccount, amount, percent);
    return ResponseEntity.ok().build();
  }
}
