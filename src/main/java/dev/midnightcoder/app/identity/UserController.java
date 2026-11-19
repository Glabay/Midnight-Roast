package dev.midnightcoder.app.identity;

import dev.midnightcoder.app.common.MidnightCacheable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Glabay | Glabay-Studios
 * @project Midnight-Coder
 * @social Discord: Glabay
 * @since 2025-12-24
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController implements MidnightCacheable {
    private final IIdentityService identityService;

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String query) {
        return getCacheableResponse(identityService.searchUsers(query));
    }
}
