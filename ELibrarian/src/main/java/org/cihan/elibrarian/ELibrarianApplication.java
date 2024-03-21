package org.cihan.elibrarian;

import org.cihan.elibrarian.auth.models.RegisterRequest;
import org.cihan.elibrarian.auth.service.AuthService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import static org.cihan.elibrarian.user.models.Role.ADMIN;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class ELibrarianApplication {

    public static void main(String[] args) {
        SpringApplication.run(ELibrarianApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(
            AuthService authService
    ) {
        return args -> {
            var admin = RegisterRequest.builder()
                    .firstname("Oğuzhan")
                    .lastname("Cihan")
                    .username("admin")
                    .email("admin@mail.com")
                    .password("password")
                    .role(ADMIN)
                    .build();
            System.out.println("Admin token: " + authService.register(admin).getAccessToken());

        };
    }

}
