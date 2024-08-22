package by.bsu.fpmi.apigateway.service;

import by.bsu.fpmi.apigateway.dto.UserReq;
import by.bsu.fpmi.apigateway.dto.UserResp;
import by.bsu.fpmi.apigateway.httpclient.AuthenticationServerClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationServerClient httpClient;
    private final JwtServiceImpl jwtService;

    public Optional<String> generateJwtIfUserValid(UserReq userReq) {
        try {
            return Optional.ofNullable(httpClient.generateJwtIfUserValid(userReq).getBody());
        } catch (FeignException exception) {
            return Optional.empty();
        }
    }

    public boolean verifyJwt(String jwt) {
            return jwtService.verifyJwt(jwt).isPresent();
    }

    public UserResp createUser(UserReq userReq) {
            ResponseEntity<UserResp> response = httpClient.createUser(userReq);
            return response.getBody();
    }
}
