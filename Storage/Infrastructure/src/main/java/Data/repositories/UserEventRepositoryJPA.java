package Data.repositories;

import Data.models.UserEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserEventRepositoryJPA extends JpaRepository<UserEvent, UUID> {
}
