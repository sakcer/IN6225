package com.in6225.project.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.in6225.project.exception.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    private JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        if (request.getRequestURI().contains("/api/v1/auth/*")) {
            filterChain.doFilter(request, response); // 跳过该过滤器
            return;
        }

        String token = request.getHeader(TOKEN_NAME);
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);

            try {
                DecodedJWT decodedJWT = jwtTokenProvider.verifyToken(token);
                String username = decodedJWT.getSubject();
                List<String> roles = decodedJWT.getClaim("roles").asList(String.class);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Invalid or expired JWT token");
                return;
            }
        } else {
//            response.setStatus(HttpStatus.UNAUTHORIZED.value());
//            response.getWriter().write("NO TOKEN");
//            return;
        }

        filterChain.doFilter(request, response);
    }
}
