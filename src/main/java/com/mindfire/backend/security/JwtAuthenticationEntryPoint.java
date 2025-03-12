package com.mindfire.backend.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    
    /**
     * Description: Handles unauthorized access attempts by sending an HTTP 401 error response.
     *
     * @param request       The HTTP request that resulted in an authentication failure.
     * @param response      The HTTP response to send the error.
     * @param authException The authentication exception that triggered this response.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }
}
