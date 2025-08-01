package Controllers;

import contracts.TransactionServiceInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import Data.models.DTO.TransactionDto;
import Data.models.transactions.Transaction;
import Data.models.transactions.enums.TransactionType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
@Tag(name="Transactions", description = "Транзакции счетов")
@RequiredArgsConstructor
public class TransactionControllers {

    private final TransactionServiceInterface transactionService;

    @GetMapping("/{accountId}/info")
    @Operation(
            summary = "Получения списка транзакций",
            description = "Получения списка транзакций для счёта, возможность фильтрации по типу транзакции"
    )
    public ResponseEntity<List<TransactionDto>> getTransactionForAccount(
            @PathVariable UUID accountId,
            @RequestParam(required = false) TransactionType txType
    ) throws Exception {
        List<Transaction> transactions = transactionService.GetListTransaction(accountId, txType);
        List<TransactionDto> dtoList = transactions.stream()
                .map(TransactionDto::from)
                .toList();
        return ResponseEntity.ok(dtoList);
    }
}
