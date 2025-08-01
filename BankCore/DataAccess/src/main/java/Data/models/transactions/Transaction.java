package Data.models.transactions;

import jakarta.persistence.*;
import lombok.*;
import Data.models.accounts.Account;
import Data.models.transactions.enums.TransactionState;
import Data.models.transactions.enums.TransactionType;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Transactions")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionState transactionState;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
}