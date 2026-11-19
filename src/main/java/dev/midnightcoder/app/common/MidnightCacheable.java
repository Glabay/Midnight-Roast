package dev.midnightcoder.app.common;

import org.springframework.http.ResponseEntity;

/**
 * @author Glabay | Glabay-Studios
 * @project Midnight-Coder
 * @social Discord: Glabay
 * @since 2026-03-31
 */
public interface MidnightCacheable {

    default ResponseEntity<?> getCacheableResponse(Object body) {
        return ResponseEntity.ok()
            .cacheControl(CachePolicies.ONE_HOUR_PUBLIC)
            .body(body);
    }

    default ResponseEntity<?> getPrivateCacheableResponse(Object body) {
        return ResponseEntity.ok()
            .cacheControl(CachePolicies.ONE_HOUR_PRIVATE)
            .body(body);
    }
}
