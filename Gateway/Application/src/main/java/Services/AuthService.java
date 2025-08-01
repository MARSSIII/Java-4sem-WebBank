package Services;

import Security.JwtUtil;
import lombok.RequiredArgsConstructor;

import models.requests.LoginRequest;
import models.JwtToken.JwtToken;
import repositories.JwtTokenRepositoryJPA;

import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    private final JwtTokenRepositoryJPA tokenRepository;

    @Transactional
    public String authenticateAndGenerateToken(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword())
        );

        JwtToken jwtToken = jwtUtil.generateToken(authentication.getName());

        tokenRepository.save(jwtToken);

        return jwtToken.getToken();
    }

    @Transactional
    public void logout(String jwt) {
        tokenRepository.deleteByToken(jwt);
    }
}