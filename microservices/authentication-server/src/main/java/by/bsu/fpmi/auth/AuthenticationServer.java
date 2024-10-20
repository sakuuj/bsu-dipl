package by.bsu.fpmi.auth;

import by.bsu.fpmi.auth.entity.User;
import by.bsu.fpmi.auth.hashing.Hasher;
import by.bsu.fpmi.auth.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableDiscoveryClient
@EnableJpaRepositories
@SpringBootApplication
public class AuthenticationServer {

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationServer.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository, Hasher hasher) {
        return args -> {
            User user = new User(0L, "admin", hasher.hash("1111"), "ADMIN");
            userRepository.saveAndFlush(user);
        };
    }
}
