package dev.midnightcoder.app.identity.dto;

import lombok.Getter;

/**
 * Commonly used roles for the platform.
 * Prefixed with ROLE_ for Spring Security compatibility.
 * 
 * @author Glabay | Glabay-Studios
 * @project Midnight-Coder
 */
@Getter
public enum UserRoleType {
    ROLE_USER("User"),
    ROLE_ADMIN("Admin"),
    ROLE_INSTRUCTOR("Instructor"),
    ROLE_OWNER("Owner"),
    ROLE_RECRUITER("Recruiter"),
    ROLE_MODERATOR("Moderator"),
    ROLE_DEVELOPER("Developer"),
    ROLE_SUPPORT("Support"),
    ROLE_PREMIUM("Premium"),
    ROLE_MIDNIGHT_YOUTUBE("MidnightTeam");

    private final String displayName;

    UserRoleType(String displayName) {
        this.displayName = displayName;
    }
}
