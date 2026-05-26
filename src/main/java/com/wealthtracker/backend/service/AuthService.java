package com.wealthtracker.backend.service;

import com.wealthtracker.backend.dto.auth.AuthRequest;
import com.wealthtracker.backend.dto.auth.AuthResponse;
import com.wealthtracker.backend.dto.auth.AuthenticatedUserResponse;
import com.wealthtracker.backend.dto.auth.RegisterRequest;
import com.wealthtracker.backend.entity.AppUser;
import com.wealthtracker.backend.exception.ResourceConflictException;
import com.wealthtracker.backend.repository.AppUserRepository;
import com.wealthtracker.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (appUserRepository.existsByEmailIgnoreCase(request.email())) {
            throw new ResourceConflictException("An account with this email already exists");
        }

        AppUser user = new AppUser();
        user.setFullName(request.fullName());
        user.setEmail(request.email().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.password()));

        AppUser savedUser = appUserRepository.save(user);
        return buildAuthResponse(savedUser);
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email().trim().toLowerCase(), request.password())
        );

        AppUser user = appUserRepository.findByEmailIgnoreCase(request.email())
            .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        return buildAuthResponse(user);
    }

    private AuthResponse buildAuthResponse(AppUser appUser) {
        var securityUser = User.withUsername(appUser.getEmail())
            .password(appUser.getPassword())
            .roles("USER")
            .build();

        return new AuthResponse(
            jwtService.generateToken(securityUser),
            new AuthenticatedUserResponse(appUser.getId(), appUser.getFullName(), appUser.getEmail())
        );
    }
}
