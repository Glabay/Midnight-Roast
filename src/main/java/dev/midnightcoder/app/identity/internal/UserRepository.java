package dev.midnightcoder.app.identity.internal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Glabay | Glabay-Studios
 * @project Midnight-Coder
 * @social Discord: Glabay
 * @since 2025-12-24
 */
@Repository
interface UserRepository extends JpaRepository<MidnightUser, String> {

    Boolean existsByEmail(String email);
    Boolean existsByNicknameIgnoreCase(String nickname);

    Optional<MidnightUser> findByEmail(String email);
    Optional<MidnightUser> findByNicknameIgnoreCase(String nickname);

    List<MidnightUser> findByNicknameContainingIgnoreCaseOrEmailContainingIgnoreCase(String nickname, String email);

}
