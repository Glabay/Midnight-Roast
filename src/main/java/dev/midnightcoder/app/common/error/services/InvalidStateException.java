package dev.midnightcoder.app.common.error.services;

import dev.midnightcoder.app.common.error.ServiceException;

/**
 * @author Glabay | Glabay-Studios
 * @project Midnight-Coder
 * @social Discord: Glabay
 * @since 2025-12-27
 */
public class InvalidStateException extends ServiceException {
    public InvalidStateException(String reason) {
        super("Operation not allowed due to invalid state: " + reason);
    }
}
