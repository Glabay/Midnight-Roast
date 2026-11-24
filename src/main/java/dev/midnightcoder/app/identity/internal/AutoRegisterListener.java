package dev.midnightcoder.app.identity.internal;

import dev.midnightcoder.app.identity.AutoRegisterProperties;
import dev.midnightcoder.app.identity.IIdentityService;
import dev.midnightcoder.app.identity.dto.UserRegistrationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author Glabay | The Midnight Coder
 * @project Midnight-Roast
 * @social Discord: Glabay
 * @website <a href="https://midnightcoder.dev">Midnight Coder</a>
 * @since 2026-11-24
 */
@Slf4j
@Component
@RequiredArgsConstructor
class AutoRegisterListener {
    private final AutoRegisterProperties autoRegisterProperties;
    private final IIdentityService identityService;

    @EventListener(ApplicationReadyEvent.class)
    void onApplicationReady() {
        if (!autoRegisterProperties.isEnabled()) {
            log.info("Auto-registration is not configured, skipping.");
            return;
        }

        var username = autoRegisterProperties.getUsername();
        if (identityService.existsByNickname(username)) {
            log.info("User '{}' already exists, skipping auto-registration.", username);
            return;
        }

        try {
            var request = new UserRegistrationRequest(
                "",
                "",
                username,
                autoRegisterProperties.getEmail(),
                autoRegisterProperties.getPassword()
            );
            identityService.register(request);
            log.info("Auto-registered user '{}' with ROLE_OWNER.", username);
        }
        catch (Exception ex) {
            log.error("Failed to auto-register user '{}': {}", username, ex.getMessage());
        }
    }
}
