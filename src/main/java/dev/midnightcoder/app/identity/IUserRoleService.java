package dev.midnightcoder.app.identity;

import java.util.List;

/**
 * @author Glabay | Glabay-Studios
 * @project Midnight-Coder
 * @social Discord: Glabay
 * @since 2026-09-29
 */
public interface IUserRoleService {

    boolean addRoleToUser(String email, String role);

    boolean removeRoleFromUser(String email, String role);

    boolean userHasRole(String email, String role);

    List<String> getRolesForEmail(String email);

    void deleteRolesForEmail(String email);

}
