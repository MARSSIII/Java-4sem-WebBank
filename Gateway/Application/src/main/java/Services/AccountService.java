package Services;

import clients.AccountWebClient;
import lombok.RequiredArgsConstructor;
import models.DTO.accounts.AccountDto;
import models.JwtToken.JwtToken;
import models.requests.ForwardRequest;
import models.requests.RefillRequest;
import models.requests.WithdrawRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountWebClient accountWebClient;

    public AccountDto createAccount(UUID userId, JwtToken jwtToken){
        return accountWebClient.createAccount(userId, jwtToken);
    }

    public List<AccountDto> getAccounts(JwtToken jwtToken){
        return accountWebClient.getAllAccounts(jwtToken);
    }
    /**
     * Получить все счета пользователя по его id
     * @param userId UUID пользователя
     * @return список счетов конкретного пользователя
     */
    public List<AccountDto> getAccountsByUserId(UUID userId, JwtToken jwtToken) {
        return accountWebClient.getAccountsByUser(userId, jwtToken);
    }

    /**
     * Получить информацию о счёте с транзакциями по его id.
     * @param accountId UUID счета
     * @return полная информация о счёте, включая транзакции
     */
    public AccountDto getAccountsByAccountID(UUID accountId, JwtToken jwtToken) {
        return accountWebClient.getAccount(accountId, jwtToken);
    }

    public void withdrawAmountForAccount(UUID accountId, WithdrawRequest withdrawRequest, JwtToken jwtToken) {
        accountWebClient.withdraw(accountId, withdrawRequest, jwtToken);
    }

    public void refillAmountForAccount(UUID accountId, RefillRequest refillRequest, JwtToken jwtToken) {
        accountWebClient.refill(accountId, refillRequest, jwtToken);
    }

    public void forwardAmountForAccount(UUID userId, ForwardRequest forwardRequest, JwtToken jwtToken) {
        accountWebClient.forward(userId, forwardRequest, jwtToken);
    }
}