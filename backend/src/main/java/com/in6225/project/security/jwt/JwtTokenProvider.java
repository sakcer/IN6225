package com.in6225.project.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private final String SECRET_KEY = "IN6225";
    // private final long EXPIRATION_TIME = 3600 * 1000;
    // private static final long ACCESS_TOKEN_EXPIRATION = 30 * 60 * 1000; // 30min
    private static final long ACCESS_TOKEN_EXPIRATION = 30 * 60 * 1000; // 30min
    private static final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000; // 7days

//    public String generateToken(Authentication authentication) {
//        String username = authentication.getName();
//        List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
//        return JWT.create()
//                .withSubject(username)
//                .withClaim("roles", roles)
//                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//                .sign(Algorithm.HMAC512(SECRET));
//    }

    // 生成 Access Token
    public String generateAccessToken(Authentication authentication) {
        String username = authentication.getName();
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .withClaim("roles", roles)
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }

    public String generateRefreshToken(Authentication authentication) {
        String username = authentication.getName();
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }


    public DecodedJWT verifyToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC512(SECRET_KEY)).build();
        return verifier.verify(token);
    }

    public String getUsernameFromToken(String token) {
        return JWT.decode(token).getSubject();
    }
}
