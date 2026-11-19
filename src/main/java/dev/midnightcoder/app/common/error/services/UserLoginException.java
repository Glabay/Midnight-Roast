package dev.midnightcoder.app.common.error.services;

import dev.midnightcoder.app.common.error.ServiceException;

/**
 * @author Glabay | Glabay-Studios
 * @project Midnight-Coder
 * @social Discord: Glabay
 * @since 2026-03-22
 */
public class UserLoginException extends ServiceException {
    public UserLoginException(String reason) {
        super("User login failed: " + reason);
    }
}
