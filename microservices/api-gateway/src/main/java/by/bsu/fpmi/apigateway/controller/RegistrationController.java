package by.bsu.fpmi.apigateway.controller;

import by.bsu.fpmi.apigateway.dto.UserReq;
import by.bsu.fpmi.apigateway.dto.UserResp;
import by.bsu.fpmi.apigateway.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class RegistrationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<UserResp> register(@RequestBody @Valid UserReq userReq) {

        UserResp userResp;
        try {
            userResp = authenticationService.createUser(userReq);
        } catch (Exception exception) {
            throw new RuntimeException("Пользователь с указанным именем уже существует");
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userResp);
    }
}
