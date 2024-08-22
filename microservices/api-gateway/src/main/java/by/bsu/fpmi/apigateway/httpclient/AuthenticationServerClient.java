package by.bsu.fpmi.apigateway.httpclient;

import by.bsu.fpmi.apigateway.dto.UserReq;
import by.bsu.fpmi.apigateway.dto.UserResp;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "auth-server",path = "/auth")
public interface AuthenticationServerClient {
    @GetMapping("public-key")
    ResponseEntity<String> getPublicKey();

    @PostMapping("/generate-jwt")
     ResponseEntity<String> generateJwtIfUserValid(@RequestBody @Valid UserReq userReq);

    @PostMapping
    ResponseEntity<UserResp> createUser(@RequestBody @Valid UserReq userReq);
}
