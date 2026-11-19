package dev.midnightcoder.app.identity.internal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Glabay | Glabay-Studios
 * @project Midnight-Coder
 * @social Discord: Glabay
 * @since 2026-01-03
 */
@Repository
interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findByEmailIgnoreCase(String email);

    void deleteByEmailIgnoreCase(String email);

}
