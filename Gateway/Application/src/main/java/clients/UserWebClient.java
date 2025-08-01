package clients;

import lombok.RequiredArgsConstructor;
import models.DTO.users.UserDto;
import models.DTO.users.enums.HairColor;
import models.DTO.users.enums.Sex;
import models.JwtToken.JwtToken;
import models.requests.CreateUserForBankCoreRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Component
public class UserWebClient {
    private final WebClient webClient;

    @Autowired
    public UserWebClient(WebClient.Builder webClientBuilder, @Value("${bank.base-url}") String bankingApiUrl) {
        this.webClient = webClientBuilder.baseUrl(bankingApiUrl).build();
    }

    public UserDto createUser(CreateUserForBankCoreRequest createUserRequest, JwtToken jwtToken){
        return webClient.post()
                .uri("/users")
                .header("Authorization", "Bearer" + jwtToken.getToken())
                .bodyValue(createUserRequest)
                .retrieve()
                .bodyToMono(UserDto.class)
                .block();
    }

    public UserDto getUserByUserId(UUID userId, JwtToken jwtToken){
        return webClient.get()
                .uri("/users/{userId}", userId)
                .header("Authorization", "Bearer " + jwtToken.getToken())
                .retrieve()
                .bodyToMono(UserDto.class)
                .block();
    }

    public List<UserDto> getAllUsers(HairColor hairColor, Sex gender, JwtToken jwtToken){
        WebClient.RequestHeadersUriSpec<?> spec = (WebClient.RequestHeadersUriSpec<?>) webClient.get().uri(uriBuilder -> {
            uriBuilder.path("/users/all");
            if (gender != null) {
                uriBuilder.queryParam("gender", gender.name());
            }
            if (hairColor != null) {
                uriBuilder.queryParam("hairColor", hairColor.name());
            }
            return uriBuilder.build();
        });

        return spec
                .header("Authorization", "Bearer " + jwtToken.getToken())
                .retrieve()
                .bodyToFlux(UserDto.class)
                .collectList()
                .block();
    }

    public void addFriend(UUID userId, String friendLogin, JwtToken jwtToken){
        webClient.post()
                .uri("/users/{userId}/friends_add", userId)
                .header("Authorization", "Bearer " + jwtToken.getToken())
                .bodyValue(friendLogin)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void removeFriend(UUID userId, String friendLogin, JwtToken jwtToken){
        webClient.delete()
                .uri("/users/{userId}/friends_remove")
                .header("Authorization", "Bearer " + jwtToken.getToken())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
