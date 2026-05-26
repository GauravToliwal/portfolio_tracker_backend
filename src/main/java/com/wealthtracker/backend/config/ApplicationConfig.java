package com.wealthtracker.backend.config;

import com.wealthtracker.backend.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final AppUserRepository appUserRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            var user = appUserRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            UserDetails userDetails = User.withUsername(user.getEmail())
                .password(user.getPassword())
                .roles("USER")
                .build();

            return userDetails;
        };
    }
}
