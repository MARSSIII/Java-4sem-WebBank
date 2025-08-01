package contracts;

import Data.models.users.User;
import Data.models.users.enums.HairColor;
import Data.models.users.enums.Sex;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Интерфейс сервиса для управления пользователями.
 * Определяет методы для создания пользователей, входа в систему, получения информации о пользователе,
 * управления друзьями и отображения информации.
 */
public interface UserServiceInterface {

    /**
     * Создает нового пользователя с указанными параметрами.
     *
     * @param userName Имя пользователя.
     * @param login Логин пользователя.
     * @param yearBirth Год рождения пользователя.
     * @param gender Пол пользователя.
     * @param hairColor Цвет волос пользователя.
     * @throws IllegalArgumentException Если имя пользователя или логин равны {@code null}.
     */
    User CreateUser(String userName, String login, Integer yearBirth, Sex gender, HairColor hairColor);

    /**
     * Получения сущности User с помощью userId
     * @param userId индификатор пользователя
     * @return сущность user соотвествующая userId
     */
    User GetUserById(UUID userId);

    /**
     * Получения списка всех пользователей в системе.
     *
     * @return List<User> список юзеров, в случае 0 кол-ва возвращается пустой список
     */
    List<User> GetListAllUsers(Sex gender, HairColor hairColor);

    /**
     * Отображает список друзей для указанного пользователя.
     *
     * @param userId пользовательский ID, для которого необходимо отобразить список друзей.
     */
    Set<User> GetFriendsForUser(UUID userId);



    Boolean IsFriendsUsers(User user, String friend);

    /**
     * Добавляет друга для указанного пользователя.
     *
     * @param user Пользователь, для которого добавляется друг.
     * @param otherUserName Имя пользователя, которого добавляют в друзья.
     * @throws IllegalArgumentException Если пользователь или имя друга равны {@code null}, или если друг не найден.
     */
    void AddFriendForUser(User user, String otherUserName) throws Exception;

    /**
     * Удаляет друга у указанного пользователя.
     *
     * @param user Пользователь, у которого удаляется друг.
     * @param otherUserLogin Имя пользователя, которого удаляют из друзей.
     * @throws IllegalArgumentException Если пользователь или имя друга равны {@code null}, или если друг не найден.
     */
    void DeleteFriendForUser(User user, String otherUserLogin) throws ClassNotFoundException;
}
