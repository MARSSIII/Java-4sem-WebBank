package Controllers;

import Services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import models.DTO.users.UserDto;
import models.DTO.accounts.AccountDto;
import Services.AccountService;
import Services.AuthService;

import models.JwtToken.JwtToken;
import models.requests.ForwardRequest;
import models.requests.RefillRequest;
import models.requests.WithdrawRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController {

    private final UserService userService;

    private final AccountService accountService;

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

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getSelf(@PathVariable UUID userId,HttpServletRequest httpServletRequest) {
        UserDto user = userService.getUserByUserId(userId, getToken(httpServletRequest));
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/accounts/{userId}")
    public ResponseEntity<List<AccountDto>> getUserAccounts(@PathVariable UUID userId, HttpServletRequest httpServletRequest) {
        List<AccountDto> accounts = accountService.getAccountsByUserId(userId, getToken(httpServletRequest));
        return ResponseEntity.ok(accounts);
    }


    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<AccountDto> getAccount(@PathVariable UUID accountId, Authentication authentication, HttpServletRequest httpServletRequest){
        String login = authentication.getName();
        AccountDto accountDto = accountService.getAccountsByAccountID(accountId, getToken(httpServletRequest));

        UserDto userDto = userService.getUserByUserId(accountDto.getOwnerId(), getToken(httpServletRequest));
        if(!login.equals(userDto.getLogin())){
            throw new AccessDeniedException("Access to this account is forbidden");
        }

        return ResponseEntity.ok(accountDto);
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/{userId}/friends/add")
    public ResponseEntity<Void> addFriend(
            @PathVariable UUID userId,
            @RequestParam String friendLogin,
            HttpServletRequest httpServletRequest
    ) {
        userService.addFriend(userId, friendLogin, getToken(httpServletRequest));

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/{userId}/friends/remove")
    public ResponseEntity<Void> removeFriend(
            @PathVariable UUID userId,
            @RequestParam String friendLogin,
            HttpServletRequest httpServletRequest) {
        userService.removeFriend(userId, friendLogin, getToken(httpServletRequest));

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/accounts/{accountId}/refill")
    public ResponseEntity<Void> refillAccount(
            @PathVariable UUID accountId,
            @RequestBody RefillRequest refillRequest,
            Authentication authentication,
            HttpServletRequest httpServletRequest)
    {
        String login = authentication.getName();
        AccountDto accountDto = accountService.getAccountsByAccountID(accountId, getToken(httpServletRequest));

        UserDto userDto = userService.getUserByUserId(accountDto.getOwnerId(), getToken(httpServletRequest));
        if(!login.equals(userDto.getLogin())){
            throw new AccessDeniedException("Access to this account is forbidden");
        }

        accountService.refillAmountForAccount(accountId, refillRequest, getToken(httpServletRequest));

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/accounts/{accountId}/withdraw")
    public ResponseEntity<Void> withdrawAccount (
            @PathVariable UUID accountId,
            @RequestBody WithdrawRequest withdrawRequest,
            Authentication authentication,
            HttpServletRequest httpServletRequest){

        String login = authentication.getName();
        AccountDto accountDto = accountService.getAccountsByAccountID(accountId, getToken(httpServletRequest));

        UserDto userDto = userService.getUserByUserId(accountDto.getOwnerId(), getToken(httpServletRequest));
        if(!login.equals(userDto.getLogin())){
            throw new AccessDeniedException("Access to this account is forbidden");
        }

        accountService.withdrawAmountForAccount(accountId, withdrawRequest, getToken(httpServletRequest));

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/accounts/{accountId}/forward")
    public ResponseEntity<Void> forwardAccount (
            @PathVariable UUID accountId,
            @RequestBody ForwardRequest forwardRequest,
            Authentication authentication,
            HttpServletRequest httpServletRequest){

        String login = authentication.getName();
        AccountDto accountDto = accountService.getAccountsByAccountID(accountId, getToken(httpServletRequest));

        UserDto userDto = userService.getUserByUserId(accountDto.getOwnerId(), getToken(httpServletRequest));
        if(!login.equals(userDto.getLogin())){
            throw new AccessDeniedException("Access to this account is forbidden");
        }

        accountService.forwardAmountForAccount(accountId, forwardRequest, getToken(httpServletRequest));

        return ResponseEntity.ok().build();
    }
}

