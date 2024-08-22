package by.bsu.fpmi.apigateway.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JwtAuthenticationConverter implements AuthenticationConverter {
    @Override
    public Authentication convert(HttpServletRequest request) {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null) {
            return null;
        }

        if (header.length() < 8 || !header.startsWith("Bearer")) {
            return null;
        }

        String jwtToken = header.substring(7);

        return new JwtAuthenticationToken(jwtToken, List.of());
    }
}
