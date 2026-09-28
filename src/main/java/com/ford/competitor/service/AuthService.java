package com.ford.competitor.service;

import com.ford.competitor.config.JwtUtils;
import com.ford.competitor.dto.LoginDto;
import com.ford.competitor.dto.TokenDto;
import com.ford.competitor.exception.InvalidCredentialsException;
import com.ford.competitor.model.User;
import com.ford.competitor.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    public TokenDto authenticate(LoginDto login) {
        String email = login.getEmail().trim().toLowerCase(Locale.ROOT);
        User user = userRepository.findByEmail(email)
                .filter(foundUser -> passwordEncoder.matches(login.getPassword(), foundUser.getPassword()))
                .orElseThrow(InvalidCredentialsException::new);

        String token = jwtUtils.generateToken(user.getEmail(), user.getRole().name());
        return new TokenDto(token, "Bearer", jwtUtils.getExpirationInSeconds());
    }
}
