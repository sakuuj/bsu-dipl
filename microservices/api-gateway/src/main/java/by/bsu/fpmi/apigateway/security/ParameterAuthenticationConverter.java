package by.bsu.fpmi.apigateway.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;

@Component
public class ParameterAuthenticationConverter implements AuthenticationConverter {

    @Override
    public Authentication convert(HttpServletRequest request) {

        String userName = request.getParameter("userName");
        String password = request.getParameter("password");

        if (userName == null || password == null) {
            return null;
        }

        return new ParameterAuthenticationToken<>(userName, password, null);
    }
}
