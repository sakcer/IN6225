package com.in6225.project.security.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.in6225.project.security.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

//@WebFilter(urlPatterns = "/api/*")
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String TOKEN_NAME = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Skip authentication for certain URI patterns (like login)
        if (request.getRequestURI().contains("/api/v1/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Get the JWT from the request header
        String token = extractToken(request);
        if (token == null) {
            handlerExceptionResolver.resolveException(request, response, null, new SecurityException("Token is missing"));
            return;
        }

        try {
            DecodedJWT decodedJWT = jwtTokenProvider.verifyToken(token); // Verify the token
            String username = decodedJWT.getSubject();
            List<String> roles = decodedJWT.getClaim("roles").asList(String.class);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, roles.stream()
                            .map(SimpleGrantedAuthority::new).collect(Collectors.toList()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            System.out.println(e);
            handlerExceptionResolver.resolveException(request, response, null, new SecurityException("Invalid JWT token"));
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_NAME);
        return (token != null && token.startsWith(BEARER_PREFIX)) ? token.substring(BEARER_PREFIX.length()) : null;
    }
}
