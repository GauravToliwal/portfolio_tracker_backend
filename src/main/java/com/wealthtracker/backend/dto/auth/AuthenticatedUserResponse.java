package com.wealthtracker.backend.dto.auth;

public record AuthenticatedUserResponse(
    Long id,
    String fullName,
    String email
) {
}
