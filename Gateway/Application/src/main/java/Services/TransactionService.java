package Services;

import clients.TransactionWebClient;
import lombok.RequiredArgsConstructor;
import models.DTO.transactions.TransactionDto;
import models.JwtToken.JwtToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionWebClient transactionWebClient;

    public List<TransactionDto> getTransactionsForAccount(UUID accountId, JwtToken jwtToken){
        return transactionWebClient.getTransactionForAccount(accountId, jwtToken);
    }
}
