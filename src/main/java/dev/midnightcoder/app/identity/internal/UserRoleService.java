package dev.midnightcoder.app.identity.internal;

import dev.midnightcoder.app.identity.IUserRoleService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Glabay | Glabay-Studios
 * @project Midnight-Coder
 * @social Discord: Glabay
 * @since 2026-01-03
 */
@Service
@RequiredArgsConstructor
class UserRoleService implements IUserRoleService {
    private static final Logger logger = LoggerFactory.getLogger(UserRoleService.class);

    private final UserRoleRepository userRoleRepository;

    @Override
    public boolean addRoleToUser(String email, String role) {
        if (userHasRole(email, role)) {
            logger.debug("User ({}) already has role: {}", email, role);
            return false;
        }
        var userRole = new UserRole();
            userRole.setEmail(email);
            userRole.setRole(role.startsWith("ROLE_") ? role : "ROLE_" + role);
        userRoleRepository.save(userRole);
        return true;
    }

    @Override
    public boolean removeRoleFromUser(String email, String role) {
        var existing = userRoleRepository.findByEmailIgnoreCase(email).stream()
                .filter(r -> r.getRole().equalsIgnoreCase(role) || r.getRole().equalsIgnoreCase(role.startsWith("ROLE_") ? role : "ROLE_" + role))
                .findFirst();
        if (existing.isPresent()) {
            userRoleRepository.delete(existing.get());
            return true;
        }
        return false;
    }

    @Override
    public boolean userHasRole(String email, String role) {
        var roles = getRolesForEmail(email);
        var targetRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;
        return roles.stream().anyMatch(r -> r.equalsIgnoreCase(role) || r.equalsIgnoreCase(targetRole));
    }

    @Override
    public List<String> getRolesForEmail(String email) {
        return userRoleRepository.findByEmailIgnoreCase(email).stream()
                .map(UserRole::getRole)
                .toList();
    }

    @Override
    public void deleteRolesForEmail(String email) {
        userRoleRepository.deleteByEmailIgnoreCase(email);
    }
}
