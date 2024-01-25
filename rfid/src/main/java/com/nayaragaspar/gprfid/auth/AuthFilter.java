package com.nayaragaspar.gprfid.auth;

import java.io.IOException;
import java.util.Objects;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthFilter extends OncePerRequestFilter {
    private final String apiKeyHeaderName;

    private final JwtFilter jwtFilter;
    private final String apiKeyHeaderValue;

    public AuthFilter(JwtFilter jwtFilter, String apiKeyHeaderName, String apiKeyHeaderValue) {
        this.jwtFilter = jwtFilter;
        this.apiKeyHeaderName = apiKeyHeaderName;
        this.apiKeyHeaderValue = apiKeyHeaderValue;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String apiKeyHeader = request.getHeader(apiKeyHeaderName);

        try {
            if (apiKeyHeaderValue.equals(apiKeyHeader)) {
                UsernamePasswordAuthenticationToken authentication = jwtFilter.validateJwt(request);

                if (!Objects.isNull(authentication)) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } else {
                System.out.println("Invalid ApiKey Value!");
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            System.out.println("Exception");

            throw e;
        }
    }
}
