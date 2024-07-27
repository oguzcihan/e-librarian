package org.cihan.elibrarian.app.security.token;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
