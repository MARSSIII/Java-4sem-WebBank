package Controllers;

import contracts.UserServiceInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import Data.models.DTO.UserDto;
import Data.models.requestBody.CreateUserRequest;
import Data.models.users.User;
import Data.models.users.enums.HairColor;
import Data.models.users.enums.Sex;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@Tag(name="Users", description = "Операции с пользователями")
@RequiredArgsConstructor
public class UserControllers {

    private final UserServiceInterface userService;

    @GetMapping("/all")
    @ApiResponse(responseCode = "200", description = "Список друзей успешно выведен")
    @Operation(summary = "Получить всех пользователей",
                description = "Возвращает список всех пользователей с возможностью фильтрации по цвету волос и гендеру")
    public ResponseEntity<List<UserDto>> getAllUsers(@RequestParam(required = false) Sex gender,
                                                     @RequestParam(required = false) HairColor hairColor) {
        List<User> users = userService.GetListAllUsers(gender, hairColor);
        List<UserDto> userDtos = users == null
                ? List.of() : users.stream().map(UserDto::from).toList();

        return ResponseEntity.ok(userDtos);
    }

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь успешно создан"),
            @ApiResponse(responseCode = "400", description = "Указаны неправильные данные в теле запроса")
    })
    @Operation(summary = "Создать нового пользователя",
            description = "Создаёт пользователя на основе JSON-запроса")
    public ResponseEntity<UUID> createUser(@RequestBody CreateUserRequest request) {
        User user = userService.CreateUser(
                request.getUserName(),
                request.getLogin(),
                request.getYearBirth(),
                request.getGender(),
                request.getHairColor()
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(user.getId());
    }

    @Operation(summary = "Получения user по идентификатору",
            description = "Получения user, соотв. указанному идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь по указанному идентификатору успешно получен"),
            @ApiResponse(responseCode = "404", description = "Пользователь по идентификатору не был найден")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserByUserId(@PathVariable UUID userId) throws ClassNotFoundException {
        User user = userService.GetUserById(userId);

        if (user == null){
            throw new ClassNotFoundException("User not found");
        }

        return ResponseEntity.ok(UserDto.from(user));
    }

    @GetMapping("/{userId}/info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация о пользователе по указанному идентификатору успешно выведена"),
            @ApiResponse(responseCode = "404", description = "Пользователь по идентификатору не был найден")
    })
    @Operation(summary = "Получение информации про пользователя",
            description = "Получения информации о пользователе с указанным userid")
    public ResponseEntity<UserDto> getInfoForUser(@PathVariable UUID userId) throws ClassNotFoundException {
        User user = userService.GetUserById(userId);

        if (user == null){
            throw new ClassNotFoundException("User not found");
        }

        UserDto uDto = UserDto
                .from(user);

        return ResponseEntity.ok(uDto);
    }

    @GetMapping("/{userId}/friends")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список друзей успешно получен"),
            @ApiResponse(responseCode = "404", description = "Пользователь по идентификатору не был найден")
    })
    @Operation(summary = "Получить всех друзей",
            description = "Получения всех друзей пользователя по userId")
    public ResponseEntity<Set<UserDto>> getFriendForUser(@PathVariable UUID userId) throws ClassNotFoundException {
        User user = userService.GetUserById(userId);
        if (user == null){
            throw new ClassNotFoundException("User not found");
        }

        Set<User> friends = userService.GetFriendsForUser(userId);
        Set<UserDto> dtos = friends
                .stream()
                .map(UserDto::from)
                .collect(Collectors.toSet());

        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/{userId}/friends_add")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Друг успешно добавлен"),
            @ApiResponse(responseCode = "404", description = "Пользователь по идентификатору не был найден")
    })
    @Operation(summary = "Добавить друга",
            description = "Добавляет пользователя с указанным логином в друзья к userId")
    public ResponseEntity<Void> addFriendForUser(@PathVariable UUID userId, @RequestParam String friendLogin) throws Exception {
        User user = userService.GetUserById(userId);

        if (user == null){
            throw new ClassNotFoundException("User not found");
        }

        userService.AddFriendForUser(user, friendLogin);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/friends_remove/{friendLogin}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Друг успешно удалён"),
            @ApiResponse(responseCode = "404", description = "Пользователь по идентификатору не был найден")
    })
    @Operation(summary = "Удаление друга",
                description = "Удаляет друга у пользователя с указанным логином к userId")
    public ResponseEntity<Void> removeFriendForUser(@PathVariable UUID userId, @PathVariable String friendLogin) throws ClassNotFoundException {
        User user = userService.GetUserById(userId);

        if (user == null){
            throw new ClassNotFoundException("User not found");
        }

        userService.DeleteFriendForUser(user, friendLogin);
        return ResponseEntity.ok().build();
    }
}
