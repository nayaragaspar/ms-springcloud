package com.nayaragaspar.gprfid.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import com.nayaragaspar.gprfid.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

public class JwtFilter {
    private final JwtService jwtService;

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public UsernamePasswordAuthenticationToken validateJwt(HttpServletRequest request) {
        String jwtValue = request.getHeader("Authorization");

        Claims tokenClaims = null;
        Object username = null;
        String token = null;

        try {
            if (jwtValue != null && jwtValue.startsWith("Bearer")) {
                token = jwtValue.substring(7);

                tokenClaims = jwtService.getAuthenticatedUser(token);
                username = jwtService.getNmUsuario();

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    if (jwtService.validateAccessToken(token)) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                username, tokenClaims, null);

                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        return authentication;
                    } else {
                        System.out.println("JWT validation fails!");
                    }
                }
            } else {
                System.out.println("Invalid Header JWT Value!");
            }
        } catch (Exception e) {
            System.out.println("Token exception: " + e.getMessage());
        }

        return null;
    }
}
