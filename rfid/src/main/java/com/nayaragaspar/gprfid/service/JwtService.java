package com.nayaragaspar.gprfid.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nayaragaspar.gprfid.exception.AuthenticationException;
import com.nayaragaspar.gprfid.exception.ValidationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    private static final String EMPTY_SPACE = " ";
    private static final Integer TOKEN_INDEX = 1;

    @Value("${app.token.secret-key}")
    private String secretKey;

    private Object nmUsuario;

    private Object perfis;

    public Claims getAuthenticatedUser(String token) {
        var tokenClaims = getClaims(token);
        this.nmUsuario = tokenClaims.get("username");
        this.perfis = tokenClaims.get("permissoes");

        System.out.println("nmUsuario: " + nmUsuario);
        return tokenClaims;

    }

    public Object getNmUsuario() {
        return nmUsuario;
    }

    public Object getPerfis() {
        return perfis;
    }

    private Claims getClaims(String token) {
        var accessToken = extractToken(token);
        try {

            return Jwts.parserBuilder().setSigningKey(generateSign()).build().parseClaimsJws(accessToken)
                    .getBody();

        } catch (Exception ex) {
            throw new AuthenticationException("Invalid token: " + ex.getMessage());
        }
    }

    public boolean validateAccessToken(String token) {
        getClaims(token);
        return true;
    }

    private String extractToken(String token) {
        if (isEmpty(token)) {
            throw new ValidationException("The access token was not informed.");
        }
        if (token.contains(EMPTY_SPACE)) {
            return token.split(EMPTY_SPACE)[TOKEN_INDEX];
        }
        return token;
    }

    private SecretKey generateSign() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

}
