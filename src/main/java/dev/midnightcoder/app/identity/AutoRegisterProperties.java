package dev.midnightcoder.app.identity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Glabay | The Midnight Coder
 * @project Midnight-Roast
 * @social Discord: Glabay
 * @website <a href="https://midnightcoder.dev">Midnight Coder</a>
 * @since 2026-11-24
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.auto-register")
public class AutoRegisterProperties {
    private String username;
    private String password;
    private String email;

    public boolean isEnabled() {
        return username != null
               && !username.isBlank()
               && password != null
               && !password.isBlank()
               && email != null
               && !email.isBlank();
    }
}
