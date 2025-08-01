package Controllers;

import Services.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import models.DTO.accounts.AccountFullInfoDto;
import models.DTO.accounts.AccountDto;
import models.DTO.transactions.TransactionDto;
import models.DTO.users.UserDto;
import models.DTO.users.enums.HairColor;
import models.DTO.users.enums.Sex;
import models.JwtToken.JwtToken;
import models.requests.CreateAdminRequest;
import models.requests.CreateUserRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;

    private final AccountService accountService;

    private final TransactionService transactionService;

    private JwtToken getToken(HttpServletRequest httpServletRequest) {
        String header = httpServletRequest.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            return JwtToken.builder()
                    .token(token)
                    .build();
        }
        throw new RuntimeException("No Bearer token found in Authorization header");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UUID> createAdmin(CreateAdminRequest createAdminRequest, HttpServletRequest httpServletRequest){
        UUID userId = userService.createAdmin(createAdminRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users")
    public ResponseEntity<UUID> createUser(@RequestBody CreateUserRequest userReq, HttpServletRequest httpServletRequest) {
        UUID userId = userService.createUser(userReq, getToken(httpServletRequest));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/accounts")
    public ResponseEntity<AccountDto> createAccount(@RequestBody UUID userId, HttpServletRequest httpServletRequest){
        AccountDto accountDto = accountService.createAccount(userId, getToken(httpServletRequest));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(accountDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/all")
    public ResponseEntity<List<UserDto>> getAllUsers(
            @RequestParam(required = false) Sex gender,
            @RequestParam(required = false) HairColor hairColor,
            HttpServletRequest httpServletRequest
    ) {
        List<UserDto> users = userService.getAllUsers(gender, hairColor, getToken(httpServletRequest));

        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/{userId} ")
    public ResponseEntity<UserDto> getUser(@PathVariable UUID userId, HttpServletRequest httpServletRequest) {
        UserDto user = userService.getUserByUserId(userId, getToken(httpServletRequest));
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/accounts/all")
    public ResponseEntity<List<AccountDto>> getAllAccounts(HttpServletRequest httpServletRequest) {
        List<AccountDto> accounts = accountService.getAccounts(getToken(httpServletRequest));
        return ResponseEntity.ok(accounts);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/accounts/{userId}")
    public ResponseEntity<List<AccountDto>> getAccountsByUserId(@PathVariable UUID userId, HttpServletRequest httpServletRequest) {
        List<AccountDto> accounts = accountService.getAccountsByUserId(userId, getToken(httpServletRequest));
        return ResponseEntity.ok(accounts);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<AccountDto> getAccountByAccountId(@PathVariable UUID accountId, HttpServletRequest httpServletRequest){
        AccountDto account = accountService.getAccountsByAccountID(accountId, getToken(httpServletRequest));
        return ResponseEntity.ok(account);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/accounts/{accountId}/withTx")
    public ResponseEntity<AccountFullInfoDto> getAccountWithTransactions(@PathVariable UUID accountId, HttpServletRequest httpServletRequest) {
        AccountDto account = accountService.getAccountsByAccountID(accountId, getToken(httpServletRequest));

        List<TransactionDto> transactions = transactionService.getTransactionsForAccount(accountId, getToken(httpServletRequest));

        AccountFullInfoDto accountFull = new AccountFullInfoDto();

        accountFull.setId(account.getId());
        accountFull.setBalance(account.getBalance());
        accountFull.setTransactions(transactions);

        return ResponseEntity.ok(accountFull);
    }
}
