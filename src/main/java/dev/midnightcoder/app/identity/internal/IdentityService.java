package dev.midnightcoder.app.identity.internal;

import dev.midnightcoder.app.common.error.services.ResourceNotFoundException;
import dev.midnightcoder.app.common.error.services.UserAlreadyExists;
import dev.midnightcoder.app.common.error.services.UserLoginException;
import dev.midnightcoder.app.identity.IIdentityService;
import dev.midnightcoder.app.identity.UserMapper;
import dev.midnightcoder.app.identity.dto.*;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author Glabay | Glabay-Studios
 * @project Midnight-Coder
 * @social Discord: Glabay
 * @since 2025-12-24
 */
@Service
@NullMarked
@RequiredArgsConstructor
class IdentityService implements IIdentityService {

    private final ApplicationEventPublisher eventPublisher;

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserRoleRepository roleRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponse register(UserRegistrationRequest request) {
        if (userRepository.existsByNicknameIgnoreCase(request.nickname()))
            throw new UserAlreadyExists("Nickname", request.nickname());

        if (userRepository.existsByEmail(request.email()))
            throw new UserAlreadyExists("User", request.email());

        var cached = createNewUser(request);
        createNewProfile(cached, request);
        assignDefaultRolesToUser(cached);

        return userMapper.toResponse(cached);
    }

    @Override
    public UserResponse login(LoginRequest request) {
        var user = findUserEntity(request.username());
        if (user == null)
            throw new UserLoginException("User not found");

        if (!matchesPassword(user, request.password()))
            throw new UserLoginException("Invalid password");

        return userMapper.toResponse(user);
    }

    @Override
    public List<UserResponse> searchUsers(String query) {
        return getByNicknameOrEmail(query, query).stream()
            .map(user -> new UserResponse(
                user.getId(),
                user.getNickname(),
                user.getEmail(),
                user.getEmail(),
                user.getCreatedAt()
            )).toList();
    }

    @Override
    @Transactional
    public void deleteUser(String userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        // Delete roles
        roleRepository.deleteByEmailIgnoreCase(user.getEmail());
        eventPublisher.publishEvent(new DeleteUserEvent(user.getEmail(), user.getId()));
        // Finally delete user
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public void updateUserStatus(String userId, ActivityStatus status) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        user.setStatus(status);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void changePassword(String userId, PasswordChangeRequest request) {
        if (!request.newPassword().equals(request.confirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);

        eventPublisher.publishEvent(new PasswordChangeEvent(user.getId(), user.getEmail(), user.getNickname()));
    }

    @Override
    public Optional<UserResponse> findUserByEmail(String email) {
        return userRepository.findByEmail(email).map(userMapper::toResponse);
    }

    @Override
    public UserResponse findUser(String details) {
        var user = findUserEntity(details);
        if (user == null) {
            throw new ResourceNotFoundException("User", details);
        }
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse findUserById(String id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse findUserByNickname(String nickname) {
        var user = userRepository.findByNicknameIgnoreCase(nickname)
                .orElseThrow(() -> new ResourceNotFoundException("User", nickname));
        return userMapper.toResponse(user);
    }

    @Override
    public Optional<UserResponse> findUserByNicknameOrNull(String nickname) {
        return userRepository.findByNicknameIgnoreCase(nickname).map(userMapper::toResponse);
    }

    @Override
    public List<UserResponse> findAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toResponse).toList();
    }

    @Override
    public List<MidnightUser> getByNicknameOrEmail(String nickname, String email) {
        return userRepository.findByNicknameContainingIgnoreCaseOrEmailContainingIgnoreCase(nickname, email);
    }

    @Override
    public long countUsers() {
        return userRepository.count();
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return userRepository.existsByNicknameIgnoreCase(nickname);
    }

    @Nullable
    private MidnightUser findUserEntity(String details) {
        if (details.contains("@")) {
            return userRepository.findByEmail(details).orElse(null);
        }
        return userRepository.findByNicknameIgnoreCase(details).orElse(null);
    }

    private void assignDefaultRolesToUser(MidnightUser user) {
        var userRole = new UserRole();
            userRole.setEmail(user.getEmail());
            userRole.setRole("ROLE_USER");
        roleRepository.save(userRole);
    }

    private void createNewProfile(MidnightUser user, UserRegistrationRequest request) {
        eventPublisher.publishEvent(new UserRegisteredEvent(
            user.getId(),
            user.getNickname(),
            request.firstName(),
            request.lastName()
        ));
    }

    private MidnightUser createNewUser(UserRegistrationRequest request) {
        var user = new MidnightUser();
        user.setEmail(request.email());
        user.setNickname(request.nickname());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        return userRepository.save(user);
    }

    private boolean matchesPassword(MidnightUser user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPasswordHash());
    }
}
