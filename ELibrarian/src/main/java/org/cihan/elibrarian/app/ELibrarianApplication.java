package org.cihan.elibrarian.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = "org.cihan.elibrarian")
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class ELibrarianApplication {

    public static void main(String[] args) {
        SpringApplication.run(ELibrarianApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner commandLineRunner(
//            AuthService authService
//    ) {
//        return args -> {
//            var admin = RegisterRequest.builder()
//                    .firstname("Oğuzhan")
//                    .lastname("Cihan")
//                    .userName("admin")
//                    .email("admin@mail.com")
//                    .password("password")
//                    .role(ADMIN)
//                    .build();
//            System.out.println("Admin token: " + authService.register(admin).getAccessToken());
//
//        };
//    }

}
