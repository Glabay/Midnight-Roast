package dev.midnightcoder.app.common.error.services;

import dev.midnightcoder.app.common.error.ServiceException;

/**
 * @author Glabay | Glabay-Studios
 * @project Midnight-Coder
 * @social Discord: Glabay
 * @since 2025-12-27
 */
public class ResourceNotFoundException extends ServiceException {
    public ResourceNotFoundException(String resource, Object identifier) {
        super(resource + " not found with identifier: " + identifier);
    }
}
