package dev.midnightcoder.app.identity.dto;

import java.time.LocalDateTime;

/**
 * @author Glabay | Glabay-Studios
 * @project Midnight-Coder
 * @social Discord: Glabay
 * @since 2025-12-24
 */
public record UserResponse(
    String id,
    String nickname,
    String email,
    String avatarUrl,
    LocalDateTime createdAt
) {
    public String getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
}
