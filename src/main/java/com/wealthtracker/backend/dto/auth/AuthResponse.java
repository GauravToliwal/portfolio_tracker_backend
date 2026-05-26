package com.wealthtracker.backend.dto.auth;

public record AuthResponse(
    String token,
    AuthenticatedUserResponse user
) {
}
