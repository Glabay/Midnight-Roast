package dev.midnightcoder.app.identity;


import dev.midnightcoder.app.identity.dto.UserResponse;
import dev.midnightcoder.app.identity.internal.MidnightUser;

/**
 * @author Glabay | Glabay-Studios
 * @project Midnight-Coder
 * @social Discord: Glabay
 * @since 2025-12-24
 */
public interface UserMapper {
    UserResponse toResponse(MidnightUser user);
}
