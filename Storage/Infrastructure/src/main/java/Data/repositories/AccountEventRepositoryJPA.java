package Data.repositories;

import Data.models.AccountEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountEventRepositoryJPA extends JpaRepository<AccountEvent, Integer> {
}
