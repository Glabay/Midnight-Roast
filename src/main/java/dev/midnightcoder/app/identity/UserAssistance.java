package dev.midnightcoder.app.identity;

import dev.midnightcoder.app.identity.dto.PasswordChangeRequest;
import dev.midnightcoder.app.identity.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Glabay | Glabay-Studios
 * @project Midnight-Coder
 * @social Discord: Glabay
 * @since 2026-03-18
 */
@Component
@RequiredArgsConstructor
public class UserAssistance {
    private final IIdentityService identityService;

    public UserResponse findUser(String details) {
        return identityService.findUser(details);
    }

    public UserResponse findUserById(String id) {
        return identityService.findUserById(id);
    }

    public UserResponse findUserByNickname(String nickname) {
        return identityService.findUserByNickname(nickname);
    }

    public UserResponse findUserByNicknameOrNull(String nickname) {
        return identityService.findUserByNicknameOrNull(nickname).orElse(null);
    }

    public void changePassword(String userId, PasswordChangeRequest request) {
        identityService.changePassword(userId, request);
    }
}
