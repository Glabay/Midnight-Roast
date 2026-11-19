package dev.midnightcoder.app.identity.internal;

import dev.midnightcoder.app.identity.UserMapper;
import dev.midnightcoder.app.identity.dto.UserResponse;
import org.springframework.stereotype.Component;

/**
 * @author Glabay | Glabay-Studios
 * @project Midnight-Coder
 * @social Discord: Glabay
 * @since 2025-12-24
 */
@Component
class UserMapperImpl implements UserMapper {

    @Override
    public UserResponse toResponse(MidnightUser user) {
        return new UserResponse(
            user.getId(),
            user.getNickname(),
            user.getEmail(),
            null,
            user.getCreatedAt()
        );
    }
}
