package Data.models.accounts;

import lombok.*;
import Data.models.transactions.Transaction;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Класс, представляющий счёт (аккаунт).
 * Содержит информацию о балансе, истории операций и владельце аккаунта.
 */

@Entity
@Table(name = "Accounts")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "ownerId", nullable = false)
    private UUID ownerId;

    @Column(name = "balance", nullable = false)
    @Builder.Default
    private Double balance = 0.0;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Transaction> transactions = new ArrayList<>();

    public void changeBalance(Double amount) {
        this.balance += amount;
    }
}
