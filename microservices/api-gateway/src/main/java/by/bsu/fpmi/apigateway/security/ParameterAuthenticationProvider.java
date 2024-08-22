package by.bsu.fpmi.apigateway.security;

import by.bsu.fpmi.apigateway.dto.UserReq;
import by.bsu.fpmi.apigateway.dto.UserResp;
import by.bsu.fpmi.apigateway.service.AuthenticationService;
import by.bsu.fpmi.apigateway.service.JwtServiceImpl;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ParameterAuthenticationProvider implements AuthenticationProvider {

    private final AuthenticationService authenticationService;
    private final JwtServiceImpl jwtService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        if (!(authentication instanceof ParameterAuthenticationToken<?> authenticationToken)) {
            return null;
        }

        String userName = (String) authenticationToken.getPrincipal();
        String password = (String) authenticationToken.getCredentials();


        Optional<String> jwt = authenticationService.generateJwtIfUserValid(new UserReq(userName, password));

        if (jwt.isEmpty()) {
            throw new BadCredentialsException("Invalid username or password");
        }

        Optional<SignedJWT> signedJWT = jwtService.verifyJwt(jwt.get());
        if (signedJWT.isEmpty()) {
            throw new RuntimeException("incorrectly signed jwt - server error");
        }
        UserResp userResp = jwtService.userRespFromJwt(signedJWT.get());

        String role = userResp.getRole();

        var successfulAuthentication = new ParameterAuthenticationToken<>(authentication.getName(),
                jwt.get(),
                new SimpleGrantedAuthority(role));
        successfulAuthentication.setAuthenticated(true);

        return successfulAuthentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ParameterAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
