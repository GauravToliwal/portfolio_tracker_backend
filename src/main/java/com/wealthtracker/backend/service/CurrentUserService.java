package com.wealthtracker.backend.service;

import com.wealthtracker.backend.entity.AppUser;
import com.wealthtracker.backend.exception.ResourceNotFoundException;
import com.wealthtracker.backend.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final AppUserRepository appUserRepository;

    public AppUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new ResourceNotFoundException("Authenticated user not found");
        }

        return appUserRepository.findByEmailIgnoreCase(authentication.getName())
            .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
    }
}
