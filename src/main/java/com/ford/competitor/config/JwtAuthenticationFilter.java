package com.ford.competitor.config;

import com.ford.competitor.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtUtils jwtUtils, UserRepository userRepository) {
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                Claims claims = jwtUtils.parseToken(token);
                String email = claims.getSubject();
                String role = claims.get("role", String.class);

                if (email == null || email.isBlank() || role == null || role.isBlank()) {
                    request.setAttribute("jwt.error", "Token JWT inválido ou incompleto");
                } else if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    userRepository.findByEmail(email)
                            .filter(user -> user.getRole().name().equals(role))
                            .ifPresentOrElse(user -> {
                                var authorities = List.of(new SimpleGrantedAuthority(user.getRole().name()));
                                var auth = new UsernamePasswordAuthenticationToken(user.getEmail(), null, authorities);
                                SecurityContextHolder.getContext().setAuthentication(auth);
                            }, () -> request.setAttribute("jwt.error", "Token JWT não corresponde a um usuário ativo"));
                }
            } catch (JwtException | IllegalArgumentException e) {
                SecurityContextHolder.clearContext();
                request.setAttribute("jwt.error", "Token JWT inválido ou expirado");
            }
        }

        filterChain.doFilter(request, response);
    }
}
