package Data.models.users;

import jakarta.persistence.*;
import lombok.*;
import Data.models.users.enums.HairColor;
import Data.models.users.enums.Sex;

import java.time.Year;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Класс, представляющий пользователя.
 * Содержит информацию о пользователе, включая логин, имя, год рождения, пол,
 * цвет волос и список друзей.
 */
@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = { "user_login" }))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "user_login", nullable = false)
  private String login;

  @Column(name = "user_name", nullable = false)
  private String name;

  @Column(name = "year_birth", nullable = false)
  private Integer yearBirth;

  @Column(name = "sex", nullable = false)
  @Enumerated(EnumType.STRING)
  private Sex gender;

  @Column(name = "haircolor", nullable = false)
  @Enumerated(EnumType.STRING)
  private HairColor hairColor;

  @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
  @JoinTable(name = "user_friends", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "friend_id"))
  @Builder.Default
  private Set<User> friends = new HashSet<>();

  @ManyToMany(mappedBy = "friends")
  @Builder.Default
  private Set<User> friendOf = new HashSet<>();

  /**
   * Добавляет друга в список друзей пользователя.
   *
   * @param friend Пользователь, которого добавляют в друзья и в его список друзей
   *               добавляется текущий пользователь.
   */
  public void addFriend(User friend) {
    if (friend == null) {
      throw new IllegalArgumentException("Friend cannot be null");
    }
    if (this.equals(friend)) {
      throw new IllegalArgumentException("Cannot add self as friend");
    }

    if (!friends.contains(friend)) {
      friends.add(friend);
      friend.getFriends().add(this);
    }
  }

  /**
   * Проверяет, является ли указанный пользователь другом.
   *
   * @param friend пользователь, которого проверяют на дружбу
   * @return {@code true}, если пользователь является другом, иначе {@code false}
   */
  public Boolean isFriend(Optional<User> friend) {
    if (friend.isEmpty() || friends.isEmpty()) {
      return false;
    }

    return this.friends.contains(friend);
  }

  /**
   * Удаляет друга из списка друзей пользователя.
   *
   * @param friend пользователь, которого удаляют из друзей и из его списка друзей
   *               тоже удаляется текущий пользователь.
   */
  public void removeFriend(User friend) {
    if (friend == null) {
      return;
    }

    if (friends.remove(friend)) {
      friend.getFriends().remove(this);
    }
  }

  private int getAge() {
    return Year.now().getValue() - yearBirth;
  }

  /**
   * Возвращает строковое представление пользователя.
   * Включает логин, имя, возраст, пол, цвет волос и количество друзей.
   *
   * @return Строковое представление пользователя.
   */
  @Override
  public String toString() {
    return "User{" +
        ", login='" + login + '\'' +
        ", name='" + name + '\'' +
        ", age=" + getAge() +
        ", gender=" + gender +
        ", hairColor=" + hairColor +
        ", friendsCount=" + friends.size() +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof User))
      return false;
    return login != null && login.equals(((User) o).getLogin());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
