package dev.midnightcoder.app.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author Glabay | Glabay-Studios
 * @project Midnight-Coder
 * @social Discord: Glabay
 * @since 2025-12-24
 */
@Getter
public abstract class ApiException extends RuntimeException {

    private final int status;
    private final String error;
    private final String message;

    protected ApiException(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
    }

    protected ApiException(HttpStatus status, String message) {
        this.status = status.value();
        this.error = status.name();
        this.message = message;
    }

}
