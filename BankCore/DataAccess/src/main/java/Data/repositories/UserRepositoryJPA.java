package Data.repositories;

import Data.models.users.User;
import Data.models.users.enums.HairColor;
import Data.models.users.enums.Sex;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для управления сущностями {@link User}.
 * Позволяет выполнять стандартные CRUD-операции, а также искать пользователей по логину и имени.
 * Взаимодействует с базой данных через Spring Data JPA.
 */
@Repository
public interface UserRepositoryJPA extends JpaRepository<User, UUID> {

    @EntityGraph(attributePaths = "friends")
    Optional<User> findUserWithFriendsById(UUID Id);

    Optional<User> findByLogin(String login);

    List<User> findByGenderAndHairColor(Sex gender, HairColor hairColor);

    List<User> findByGender(Sex gender);

    List<User> findByHairColor(HairColor hairColor);
}