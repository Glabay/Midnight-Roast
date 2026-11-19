package dev.midnightcoder.app.common.error.services;

import dev.midnightcoder.app.common.error.ServiceException;

/**
 * @author Glabay | Glabay-Studios
 * @project Midnight-Coder
 * @social Discord: Glabay
 * @since 2026-03-22
 */
public class UserAlreadyExists extends ServiceException {
    public UserAlreadyExists(String resource, Object identifier) {
        super(resource + " exists with identifier: " + identifier);
    }
}
