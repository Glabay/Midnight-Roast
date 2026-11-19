package dev.midnightcoder.app.common.security.jwt.refresh;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Glabay | Glabay-Studios
 * @project Midnight-Coder
 * @social Discord: Glabay
 * @since 2026-03-23
 */
@Component
@RequiredArgsConstructor
public class RefreshTokenCleanupTask {
    private static final Logger log = LoggerFactory.getLogger(RefreshTokenCleanupTask.class);
    private final RefreshTokenService refreshTokenService;

    /// Runs daily at 3 AM
    @Scheduled(cron = "0 0 3 * * *")
    public void performCleanup() {
        var removed = refreshTokenService.cleanupExpired();
        log.info("Removed {} expired refresh tokens", removed);
    }
}
