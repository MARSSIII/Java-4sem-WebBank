package clients;

import lombok.RequiredArgsConstructor;
import models.DTO.transactions.TransactionDto;
import models.JwtToken.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Component
public class TransactionWebClient {
    private final WebClient webClient;

    @Autowired
    public TransactionWebClient(WebClient.Builder webClientBuilder, @Value("${bank.base-url}") String bankingApiUrl) {
        this.webClient = webClientBuilder.baseUrl(bankingApiUrl).build();
    }

    public List<TransactionDto> getTransactionForAccount(UUID accountId, JwtToken jwtToken) {
        return webClient.get()
                .uri("/transactions/{accountId}/info", accountId)
                .header("Authorization", "Bearer " + jwtToken.getToken())
                .retrieve()
                .bodyToFlux(TransactionDto.class)
                .collectList()
                .block();
    }
}
