package by.bsu.fpmi.apigateway.security;

import by.bsu.fpmi.apigateway.dto.UserResp;
import by.bsu.fpmi.apigateway.service.JwtServiceImpl;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtServiceImpl jwtService;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!(authentication instanceof JwtAuthenticationToken jwtAuthenticationToken)) {
            return null;
        }

        String jwt = (String)jwtAuthenticationToken.getPrincipal();
        Optional<SignedJWT> signedJWT = jwtService.verifyJwt(jwt);
        if (signedJWT.isEmpty()) {
            throw new BadCredentialsException("Invalid jwt");
        }

        UserResp userResp = jwtService.userRespFromJwt(signedJWT.get());

        GrantedAuthority role = new SimpleGrantedAuthority(userResp.getRole());

        var successfulAuth = new JwtAuthenticationToken(jwt, List.of(role));
        jwtAuthenticationToken.setDetails(userResp);
        successfulAuth.setAuthenticated(true);

        return successfulAuth;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
