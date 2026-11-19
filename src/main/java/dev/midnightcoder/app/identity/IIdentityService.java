package dev.midnightcoder.app.identity;


import dev.midnightcoder.app.identity.dto.*;
import dev.midnightcoder.app.identity.internal.MidnightUser;

import java.util.List;
import java.util.Optional;

/**
 * @author Glabay | Glabay-Studios
 * @project Midnight-Coder
 * @social Discord: Glabay
 * @since 2026-09-29
 */
public interface IIdentityService {
    Optional<UserResponse> findUserByEmail(String email);
    Optional<UserResponse> findUserByNicknameOrNull(String nickname);

    UserResponse register(UserRegistrationRequest request);
    UserResponse login(LoginRequest request);
    UserResponse findUser(String details);
    UserResponse findUserById(String id);
    UserResponse findUserByNickname(String nickname);

    List<UserResponse> searchUsers(String query);
    List<UserResponse> findAllUsers();

    List<MidnightUser> getByNicknameOrEmail(String nickname, String email);

    void deleteUser(String userId);
    void updateUserStatus(String userId, ActivityStatus status);
    void changePassword(String userId, PasswordChangeRequest request);

    long countUsers();

    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);

}
