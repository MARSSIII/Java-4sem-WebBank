package Services;

import clients.UserWebClient;
import lombok.RequiredArgsConstructor;
import models.DTO.users.UserDto;
import models.DTO.users.enums.HairColor;
import models.DTO.users.enums.Sex;
import models.JwtToken.JwtToken;
import models.Client.enums.Role;
import models.requests.CreateAdminRequest;
import models.requests.CreateUserForBankCoreRequest;
import models.requests.CreateUserRequest;
import models.Client.Client;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repositories.ClientRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final ClientRepository clientRepository;

    private final UserWebClient userWebClient;

    /**
     * Прокси-метод для создания пользователя через основной банковский сервис.
     * @return id созданного пользователя (Long)
     */
    @Transactional
    public UUID createUser(CreateUserRequest userReq, JwtToken jwtToken) {
        if (clientRepository.findByLogin(userReq.getLogin()).isPresent()) {
            throw new IllegalArgumentException("Username already exists in Gateway");
        }

        CreateUserForBankCoreRequest createUserForBankCoreRequest = new CreateUserForBankCoreRequest(
                userReq.getUserName(),
                userReq.getLogin(),
                userReq.getYearBirth(),
                userReq.getGender(),
                userReq.getHairColor()
        );

        UserDto userDto = userWebClient.createUser(createUserForBankCoreRequest, jwtToken);

        Client client = Client.builder()
                .id(userDto.getId())
                .login(userReq.getLogin())
                .password(passwordEncoder.encode(userReq.getPassword()))
                .role(Role.CLIENT)
                .build();

        clientRepository.save(client);

        return userDto.getId();
    }


    @Transactional
    public UUID createAdmin(CreateAdminRequest createAdminRequest){
        Client client = Client.builder()
                .id(UUID.randomUUID())
                .login(createAdminRequest.getLogin())
                .password(passwordEncoder.encode(createAdminRequest.getPassword()))
                .role(Role.ADMIN)
                .build();

        clientRepository.save(client);

        return client.getId();
    }

    /**
     * Получение списка пользователей через прокси-запрос к банку с фильтрами по полу и цвету волос
     */
    public List<UserDto> getAllUsers(Sex gender, HairColor hairColor, JwtToken jwtToken) {
        return userWebClient.getAllUsers(hairColor, gender, jwtToken);
    }

    public UserDto getUserByUserId(UUID userId, JwtToken jwtToken) {
        return userWebClient.getUserByUserId(userId, jwtToken);
    }

    public void addFriend(UUID userId, String friendLogin, JwtToken jwtToken) {
        userWebClient.addFriend(userId, friendLogin, jwtToken);
    }

    public void removeFriend(UUID userId, String friendLogin, JwtToken jwtToken) {
        userWebClient.removeFriend(userId, friendLogin, jwtToken);
    }
}