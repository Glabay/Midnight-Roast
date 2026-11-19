package dev.midnightcoder.app.common.error.services;

import dev.midnightcoder.app.common.error.ServiceException;

/**
 * @author Glabay | Glabay-Studios
 * @project Midnight-Coder
 * @social Discord: Glabay
 * @since 2025-12-27
 */
public class UnauthorizedOperationException extends ServiceException {
    public UnauthorizedOperationException(String action) {
        super("You are not allowed to perform this action: " + action);
    }
}
