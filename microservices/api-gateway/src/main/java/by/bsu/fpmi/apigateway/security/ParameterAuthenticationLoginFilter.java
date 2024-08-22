package by.bsu.fpmi.apigateway.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.rmi.RemoteException;

@Component
@RequiredArgsConstructor
public class ParameterAuthenticationLoginFilter extends OncePerRequestFilter {

    public static final String ON_LOGIN_ATTRIBUTE = "onLoginAttr";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Object onLoginAttribute = request.getAttribute(ON_LOGIN_ATTRIBUTE);

        if (onLoginAttribute == null) {
            filterChain.doFilter(request, response);
            return;
        }

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof ParameterAuthenticationToken<?>)) {
            throw new RemoteException("incorrect type of authentication");
        }

        response.getWriter().write(authentication.getCredentials().toString());
    }
}
