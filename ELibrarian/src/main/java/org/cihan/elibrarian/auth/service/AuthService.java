package org.cihan.elibrarian.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.cihan.elibrarian.auth.models.AuthResponse;
import org.cihan.elibrarian.auth.models.LoginRequest;
import org.cihan.elibrarian.auth.models.RegisterRequest;
import org.cihan.elibrarian.exceptions.GenException;
import org.cihan.elibrarian.security.jwt.JwtService;
import org.cihan.elibrarian.security.token.Token;
import org.cihan.elibrarian.security.token.TokenType;
import org.cihan.elibrarian.user.models.User;
import org.cihan.elibrarian.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest loginRequest) {
        log.info("Login request: {}", loginRequest);
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUserName(),
                            loginRequest.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new GenException("Invalid username or password", HttpStatus.BAD_REQUEST.value());
        }

        var user = userRepository.findByUserName(loginRequest.getUserName())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthResponse register(RegisterRequest registerRequest) {
        User user = User.builder()
                .firstname(registerRequest.getFirstname())
                .lastname(registerRequest.getLastname())
                .userName(registerRequest.getUserName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getRole())
                .build();

        Boolean existsByUser = userRepository.existsByUserNameOrEmail(user.getUsername(), user.getEmail());
        if (existsByUser) {
            throw new GenException("User already exists", HttpStatus.BAD_REQUEST.value());
        }
        User savedUser = userRepository.save(user);
        Token jwtToken = jwtService.generateToken(savedUser);
        Token refreshToken = jwtService.generateRefreshToken(savedUser);

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        final String refreshTokenString = authHeader.substring(7);
        Token refreshToken = Token.builder()
                .token(refreshTokenString)
                .tokenType(TokenType.REFRESH)
                .build();
        username = jwtService.extractUsername(authHeader.substring(7));
        if (username != null) {
            var user = this.userRepository.findByUserName(username).orElseThrow();
            if (jwtService.isTokenValid(refreshToken.getToken(), user)) {
                Token accessToken = jwtService.generateToken(user);
                var authResponse = AuthResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
