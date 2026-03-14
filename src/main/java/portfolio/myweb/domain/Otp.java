package portfolio.myweb.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Otp {

    @Id
    private String email;

    private String code;

    private LocalDateTime expiresAt;

    private boolean verified;
}