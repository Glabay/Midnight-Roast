package dev.midnightcoder.app.common.error;

/**
 * @author Glabay | Glabay-Studios
 * @project Midnight-Coder
 * @social Discord: Glabay
 * @since 2025-12-24
 */
public abstract class ServiceException extends RuntimeException {
    protected ServiceException(String message) {
        super(message);
    }
    protected ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
