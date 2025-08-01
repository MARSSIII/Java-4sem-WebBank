package repositories;

import models.JwtToken.JwtToken;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JwtTokenRepositoryJPA extends JpaRepository<JwtToken, String> {
    Optional<JwtToken> findByToken(String token);

    void deleteByToken(String token);

    boolean existsByToken(JwtToken jwt);
}