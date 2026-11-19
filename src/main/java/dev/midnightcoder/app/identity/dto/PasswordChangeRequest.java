package dev.midnightcoder.app.identity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @author Glabay | The Midnight Coder
 * @project Midnight-Coder
 * @social Discord: Glabay
 * @website <a href="https://midnightcoder.dev">Midnight Coder</a>
 * @since 2026-10-10
 */
public record PasswordChangeRequest(
    @NotBlank(message = "New password cannot be blank")
    @Size(
        min = 6,
        max = 120,
        message = "Password must be between 6 and 120 characters"
    )
    String newPassword,

    @NotBlank(message = "Password confirmation cannot be blank")
    String confirmPassword
) {}
