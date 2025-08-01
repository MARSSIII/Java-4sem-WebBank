package Services;

import contracts.UserServiceInterface;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import Data.models.users.User;
import Data.models.users.enums.HairColor;
import Data.models.users.enums.Sex;
import Data.repositories.UserRepositoryJPA;

import java.time.Year;
import java.util.*;

/**
 * Сервис для управления пользователями.
 * Предоставляет методы для создания пользователей, входа в систему, получения информации о пользователе,
 * управления друзьями и отображения информации.
 */
@Service
@AllArgsConstructor
public class UserService implements UserServiceInterface {

    final UserRepositoryJPA userRepository;

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
    @Override
    @Transactional
    public User CreateUser(String userName, String login, Integer yearBirth, Sex gender, HairColor hairColor) {
        if (userName.isEmpty() || userName.equals("string") || login.equals("string") || login.isEmpty() || yearBirth == null || gender == null|| hairColor == null){
            throw new IllegalArgumentException("Parametrs cannot be null");
        }

        if (yearBirth > Year.now().getValue()){
            throw new IllegalArgumentException("The year of birth is more than possible");
        }

        User user = User.builder()
                .login(login)
                .name(userName)
                .yearBirth(yearBirth)
                .hairColor(hairColor)
                .gender(gender)
                .build();

        userRepository.save(user);
        return user;
    }

    /**
     * Получения сущности User с помощью userId
     * @param userId идентификатор пользователя
     * @return сущность user соотв. userId
     */
    @Override
    public User GetUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElse(null);
    }


    /**
     * Возвращает список всех юзеров в системе
     *
     * @return список User в системе
     */
    @Override
    public List<User> GetListAllUsers(Sex gender, HairColor hairColor) {
        if (gender == null && hairColor == null){
            return userRepository.findAll();
        }

        if (gender != null && hairColor == null){
            return userRepository.findByGender(gender);
        }

        if (gender == null){
            return userRepository.findByHairColor(hairColor);
        }

        return userRepository.findByGenderAndHairColor(gender, hairColor);
    }

    /**
     * Отображает список друзей для указанного пользователя.
     *
     * @param userId Идентификатор пользователя, для которого необходимо отобразить список друзей.
     */
    @Override
    public Set<User> GetFriendsForUser(UUID userId) {
        return userRepository.findUserWithFriendsById(userId)
                .map(User::getFriends)
                .orElse(Collections.emptySet());
    }

    /**
     * Проверяет являться ли пользователь другом
     *
     * @param user пользователь, для которого проверяется друг.
     * @param friendLogin логин друга, проверка, что этот логин является другом
     * @return true - является другом, false - не является другом
     */
    @Override
    public Boolean IsFriendsUsers(User user, String friendLogin) {
        Optional<User> userWithFriends = userRepository.findUserWithFriendsById(user.getId());

        return userWithFriends.map(value -> value
                .getFriends()
                .stream()
                .anyMatch(friend -> friend.getLogin().equals(friendLogin)))
                .orElse(false);
    }

    /**
     * Добавляет друга для указанного пользователя.
     *
     * @param user Пользователь, для которого добавляется друг.
     * @param friendLogin Login пользователя, которого добавляют в друзья.
     * @throws IllegalArgumentException Если пользователь или имя друга равны {@code null}, или если друг не найден.
     */
    @Override
    @Transactional
    public void AddFriendForUser(User user, String friendLogin) throws Exception {

        User friend = userRepository.findByLogin(friendLogin)
                .orElseThrow(() -> new ClassNotFoundException("Friend not found"));

        if (user.equals(friend)) {
            throw new Exception("Cannot add yourself as a friend");
        }

        User managedUser = userRepository.findUserWithFriendsById(user.getId())
                .orElseThrow(() -> new ClassNotFoundException("User not found"));

        if (managedUser.getFriends().contains(friend)) {
            return;
        }

        managedUser.getFriends().add(friend);
        friend.getFriends().add(managedUser);

        userRepository.save(managedUser);
        userRepository.save(friend);
    }

    /**
     * Удаляет друга у указанного пользователя.
     *
     * @param user Пользователь, у которого удаляется друг.
     * @param otherUserLogin Имя пользователя, которого удаляют из друзей.
     * @throws IllegalArgumentException Если пользователь или имя друга равны {@code null}, или если друг не найден.
     */
    @Override
    @Transactional
    public void DeleteFriendForUser(User user, String otherUserLogin) throws ClassNotFoundException {

        User friend = userRepository.findByLogin(otherUserLogin)
                .orElseThrow(() -> new ClassNotFoundException("Friend not found"));

        User managedUser = userRepository.findUserWithFriendsById(user.getId())
                .orElseThrow(() -> new ClassNotFoundException("User not found"));

        managedUser.getFriends().remove(friend);
        friend.getFriends().remove(managedUser);

        userRepository.save(managedUser);
        userRepository.save(friend);
    }
}
