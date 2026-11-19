package dev.midnightcoder.app.identity;

import dev.midnightcoder.app.identity.dto.LoginRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author Glabay | The Midnight Coder
 * @project Midnight-Coder
 * @social Discord: Glabay
 * @website <a href="https://midnightcoder.dev">Midnight Coder</a>
 * @since 2026-09-30
 */
public interface ICustomUserDetailsService extends UserDetailsService {
    UserDetails loadAuthenticatedUser(LoginRequest request);
}
