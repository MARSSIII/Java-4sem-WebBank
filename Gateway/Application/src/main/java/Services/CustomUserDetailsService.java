package Services;

import lombok.RequiredArgsConstructor;
import models.Client.Client;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import repositories.ClientRepository;

@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Client client = clientRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Client not found with login: " + login));

        return org.springframework.security.core.userdetails.User
                .withUsername(client.getLogin())
                .password(client.getPassword())
                .roles(client.getRole().name())
                .build();
    }
}
