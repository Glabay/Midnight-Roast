package dev.midnightcoder.app.identity.internal;

import dev.midnightcoder.app.common.error.services.UnauthorizedOperationException;
import dev.midnightcoder.app.identity.ICustomUserDetailsService;
import dev.midnightcoder.app.identity.IUserRoleService;
import dev.midnightcoder.app.identity.dto.ActivityStatus;
import dev.midnightcoder.app.identity.dto.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Glabay | Glabay-Studios
 * @project Midnight-Coder
 * @social Discord: Glabay
 * @since 2026-03-23
 */
@Service
@NullMarked
@RequiredArgsConstructor
class CustomUserDetailsService implements ICustomUserDetailsService {
    private final UserRepository userRepository;
    private final IUserRoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MidnightUser user;
        if (username.contains("@")) {
            user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        } else {
            user = userRepository.findByNicknameIgnoreCase(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        }
        return userToUserDetails(user);
    }

    @Override
    public UserDetails loadAuthenticatedUser(LoginRequest request) {
        return userRepository.findByNicknameIgnoreCase(request.username())
            .or(() -> userRepository.findByEmail(request.username()))
            .filter(user -> matchesPassword(user, request.password()))
            .map(this::userToUserDetails)
            .orElseThrow(() -> new UnauthorizedOperationException("Login"));
    }

    private boolean matchesPassword(MidnightUser user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPasswordHash());
    }

    private User userToUserDetails(MidnightUser user) {
        boolean enabled = user.getStatus() != ActivityStatus.INACTIVE;
        boolean accountNonLocked = user.getStatus() != ActivityStatus.SUSPENDED;

        return new User(
            user.getEmail(),
            user.getPasswordHash(),
            enabled,
            true,
            true,
            accountNonLocked,
            getAuthorities(user)
        );
    }

    private List<SimpleGrantedAuthority> getAuthorities(MidnightUser user) {
        var cachedRoles = roleService.getRolesForEmail(user.getEmail());
        var authorities = new ArrayList<>(cachedRoles.stream()
            .map(this::getAuthority)
            .toList());

        if (authorities.isEmpty())
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return authorities;
    }

    private SimpleGrantedAuthority getAuthority(String role) {
        return new SimpleGrantedAuthority(role.startsWith("ROLE_") ? role : "ROLE_" + role);
    }
}
