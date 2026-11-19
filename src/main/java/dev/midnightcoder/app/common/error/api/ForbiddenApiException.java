package dev.midnightcoder.app.common.error.api;

import dev.midnightcoder.app.common.error.ApiException;
import org.springframework.http.HttpStatus;

/**
 * @author Glabay | Glabay-Studios
 * @project Midnight-Coder
 * @social Discord: Glabay
 * @since 2025-12-27
 */
public class ForbiddenApiException extends ApiException {
    public ForbiddenApiException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}
