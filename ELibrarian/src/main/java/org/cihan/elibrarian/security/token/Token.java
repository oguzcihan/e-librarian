package org.cihan.elibrarian.security.token;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cihan.elibrarian.user.models.User;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {

    public String token;

    @Enumerated(EnumType.STRING)
    public TokenType tokenType;

    public Long expiration;

}
