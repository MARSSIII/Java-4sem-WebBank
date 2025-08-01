package clients;

import lombok.RequiredArgsConstructor;
import models.DTO.accounts.AccountDto;
import models.JwtToken.JwtToken;
import models.requests.ForwardRequest;
import models.requests.RefillRequest;
import models.requests.WithdrawRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;


@Component
public class AccountWebClient {
    private final WebClient webClient;

    @Autowired
    public AccountWebClient(WebClient.Builder webClientBuilder, @Value("${bank.base-url}") String bankingApiUrl) {
        this.webClient = webClientBuilder.baseUrl(bankingApiUrl).build();
    }

    public AccountDto createAccount(UUID userId, JwtToken jwtToken){
        return webClient.post()
                .uri("/accounts")
                .header("Authorization", "Bearer " + jwtToken.getToken())
                .bodyValue(userId)
                .retrieve()
                .bodyToMono(AccountDto.class)
                .block();
    }

    public List<AccountDto> getAccountsByUser(UUID userId, JwtToken jwtToken){
        return webClient.get()
                .uri("/accounts/{userId}/all", userId)
                .header("Authorization", "Bearer " + jwtToken.getToken())
                .retrieve()
                .bodyToFlux(AccountDto.class)
                .collectList()
                .block();
    }

    public List<AccountDto> getAllAccounts(JwtToken jwtToken){
        return webClient.get()
                .uri("accounts/all")
                .header("Authorization", "Bearer " + jwtToken.getToken())
                .retrieve()
                .bodyToFlux(AccountDto.class)
                .collectList()
                .block();
    }

    public AccountDto getAccount(UUID accountId, JwtToken jwtToken){
        return webClient.get()
                .uri("/accounts/{accountId}", accountId)
                .header("Authorization", "Bearer " + jwtToken.getToken())
                .retrieve()
                .bodyToMono(AccountDto.class)
                .block();
    }

    public void refill(UUID accountId, RefillRequest refillRequest, JwtToken jwtToken){
        webClient.post()
                .uri("/accounts/{accountId}/refill", accountId)
                .header("Authorization", "Bearer " + jwtToken.getToken())
                .bodyValue(refillRequest)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void withdraw(UUID accountId, WithdrawRequest withdrawRequest, JwtToken jwtToken){
        webClient.post()
                .uri("accounts/{accountId}/withdraw", accountId)
                .header("Authorization", "Bearer " + jwtToken.getToken())
                .bodyValue(withdrawRequest)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void forward(UUID accountId, ForwardRequest forwardRequest, JwtToken jwtToken){
        webClient.post()
                .uri("accounts/{accountId}/forward", accountId)
                .header("Authorazation", "Bearer " + jwtToken.getToken())
                .bodyValue(forwardRequest)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
