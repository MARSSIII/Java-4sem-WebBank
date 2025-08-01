package models.JwtToken;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class JwtToken {

    @Id
    private String token;

    private Instant issuedAt;
}