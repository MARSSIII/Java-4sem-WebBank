package Security;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import models.JwtToken.JwtToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import repositories.JwtTokenRepositoryJPA;

import java.time.Instant;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {

  @Value("${jwt.secret}")
  private String secret;

  private final JwtTokenRepositoryJPA tokenRepository;

  @Transactional
  public JwtToken generateToken(String username) {
    Instant issuedAt = Instant.now();

    String token = Jwts.builder()
              .setSubject(username)
              .setIssuedAt(Date.from(issuedAt))
              .signWith(SignatureAlgorithm.HS256, secret.getBytes())
              .compact();

    JwtToken jwtToken = JwtToken.builder()
              .token(token)
              .issuedAt(issuedAt)
              .build();

    tokenRepository.save(jwtToken);

    return jwtToken;
  }

  public String extractLogin(JwtToken jwtToken) {
    String token = jwtToken.getToken();

    return Jwts.parser()
            .setSigningKey(secret.getBytes())
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
  }

  public Boolean isTokenInRepository(JwtToken jwtToken){
      return tokenRepository.findByToken(jwtToken.getToken()).isPresent();
  }

  public Boolean validateToken(JwtToken jwtToken, String username) {
    final String extractedLogin = extractLogin(jwtToken);

    return extractedLogin.equals(username) && isTokenInRepository(jwtToken);
  }
}