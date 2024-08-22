package by.bsu.fpmi.auth.controller;

import by.bsu.fpmi.auth.dto.UserReq;
import by.bsu.fpmi.auth.dto.UserResp;
import by.bsu.fpmi.auth.entity.User;
import by.bsu.fpmi.auth.hashing.Hasher;
import by.bsu.fpmi.auth.repository.UserRepository;
import by.bsu.fpmi.auth.service.JwtService;
import by.bsu.fpmi.auth.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;


    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository, Hasher hasher) {
        return args -> {
            User user = new User(0L, "admin", hasher.hash("1111"), "ADMIN");
            userRepository.saveAndFlush(user);
        };
    }

    @PostMapping("/verify-jwt")
    public ResponseEntity<Void> verifyJwt(@RequestBody @NotBlank String jwt) {
        if (jwtService.verifyJwt(jwt)) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .build();
    }

    @PostMapping("/generate-jwt")
    public ResponseEntity<String> generateJwtIfUserValid(@RequestBody @Valid UserReq userReq) {
        return userService.generateJwtIfUserValid(userReq)
                .map(userResp -> ResponseEntity
                        .status(HttpStatus.OK)
                        .body(userResp))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .build());
    }

    @PostMapping
    public ResponseEntity<UserResp> createUser(@RequestBody @Valid UserReq userReq) {
        return userService.saveUser(userReq)
                .map(userResp -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(userResp))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .build());
    }

    @GetMapping("/public-key")
    public ResponseEntity<String> getPublicKey() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jwtService.getPublicKey());
    }
}
