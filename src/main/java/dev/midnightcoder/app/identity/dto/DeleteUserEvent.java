package dev.midnightcoder.app.identity.dto;

/**
 * @author Glabay | The Midnight Coder
 * @project Midnight-Coder
 * @social Discord: Glabay
 * @website <a href="https://midnightcoder.dev">Midnight Coder</a>
 * @since 2026-09-30
 */
public record DeleteUserEvent(
    String email,
    String userId
) {}
