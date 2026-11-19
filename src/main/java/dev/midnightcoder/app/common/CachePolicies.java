package dev.midnightcoder.app.common;

import org.springframework.http.CacheControl;

import java.util.concurrent.TimeUnit;

/**
 * @author Glabay | The Midnight Coder
 * @project Midnight-Coder
 * @social Discord: Glabay
 * @website <a href="https://midnightcoder.dev">Midnight Coder</a>
 * @since 2026-10-18
 */
public final class CachePolicies {

    public static final CacheControl ONE_HOUR_PUBLIC =
        CacheControl.maxAge(1, TimeUnit.HOURS)
            .cachePublic()
            .noTransform();

    public static final CacheControl ONE_HOUR_PRIVATE =
        CacheControl.maxAge(1, TimeUnit.HOURS);

    public static final CacheControl NO_CACHE =
        CacheControl.noStore();

    private CachePolicies() {}

}
