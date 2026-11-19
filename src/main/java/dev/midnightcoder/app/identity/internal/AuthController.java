package dev.midnightcoder.app.identity.internal;

import dev.midnightcoder.app.common.error.services.UserAlreadyExists;
import dev.midnightcoder.app.common.security.CookieHelper;
import dev.midnightcoder.app.common.security.jwt.JwtService;
import dev.midnightcoder.app.common.security.jwt.refresh.RefreshTokenService;
import dev.midnightcoder.app.identity.ICustomUserDetailsService;
import dev.midnightcoder.app.identity.IIdentityService;
import dev.midnightcoder.app.identity.dto.LoginRequest;
import dev.midnightcoder.app.identity.dto.UserRegistrationRequest;
import dev.midnightcoder.app.identity.dto.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Glabay | Glabay-Studios
 * @project Midnight-Coder
 * @social Discord: Glabay
 * @since 2025-12-27
 */
@Controller
@RequiredArgsConstructor
public class AuthController {
    private final IIdentityService identityService;
    private final ICustomUserDetailsService userDetailsService;
    private final CookieHelper cookieHelper;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Value("${jwt.access-ttl-minutes}")
    private int accessTtlMinutes;
    @Value("${jwt.refresh-ttl-days}")
    private int refreshTtlDays;

    @GetMapping("/login")
    public String login(Authentication authentication) {
        if (authentication != null &&
            authentication.isAuthenticated() &&
            !(authentication instanceof AnonymousAuthenticationToken)
        ) return "redirect:/";
        return "auth/login";
    }

    @GetMapping("/register")
    public String register(Authentication authentication) {
        if (authentication != null &&
            authentication.isAuthenticated() &&
            !(authentication instanceof AnonymousAuthenticationToken)
        ) return "redirect:/";
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute UserRegistrationRequest request,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", bindingResult.getFieldError().getDefaultMessage());
            return "auth/register";
        }
        try {
            var registration = identityService.register(request);

            if (Objects.isNull(registration)) {
                model.addAttribute("error", "Email already exists or registration failed.");
                return "auth/register";
            }
        }
        catch (UserAlreadyExists userExists) {
            model.addAttribute("error", userExists.getMessage());
            return "auth/register";
        }
        return "redirect:/login?registered=true";
    }

    @ResponseBody
    @PostMapping(
        value = "/auth/login",
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserResponse> loginJson(HttpServletRequest request,
                                                  HttpServletResponse response,
                                                  @Valid @RequestBody LoginRequest loginRequest,
                                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseEntity.badRequest()
                .build();

        return handleLogin(request, response, loginRequest);
    }

    @ResponseBody
    @PostMapping(
        value = "/auth/login",
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    public ResponseEntity<UserResponse> loginForm(HttpServletRequest request,
                                                  HttpServletResponse response,
                                                  @Valid @ModelAttribute LoginRequest loginRequest,
                                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseEntity.badRequest()
                .build();

        return handleLogin(request, response, loginRequest);
    }

    private ResponseEntity<UserResponse> handleLogin(HttpServletRequest request,
                                                     HttpServletResponse response,
                                                     LoginRequest loginRequest) {
        try {
            var user = userDetailsService.loadAuthenticatedUser(loginRequest);
            var ip = request.getRemoteAddr();
            // TODO: Login Attempt service
            var roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

            var jwt = jwtService.generateAccessToken(user.getUsername(), roles);
            var refreshToken = refreshTokenService.issue(user.getUsername(), request.getHeader("User-Agent"), ip);

            cookieHelper.setAccessCookie(response, jwt, Duration.ofMinutes(accessTtlMinutes));
            cookieHelper.setRefreshCookie(response, refreshToken.rawToken(), Duration.ofDays(refreshTtlDays));
        }
        catch (Exception e) {
            if (isFormLike(request))
                return ResponseEntity.status(HttpStatus.SEE_OTHER)
                    .header("Location", "/login?error=true")
                    .build();
            throw e;
        }
        if (isFormLike(request))
            return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .header("Location", "/?loginSuccess=true")
                .build();

        return ResponseEntity.noContent().build();
    }

    /**
     * This method handles the registration process.
     */
    @ResponseBody
    @PostMapping("/auth/registry")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRegistrationRequest request) {
        var registration = identityService.register(request);
        if (Objects.isNull(registration))
            return ResponseEntity.badRequest().build();

        return ResponseEntity.status(HttpStatus.SEE_OTHER)
            .header("Location", "/login?registered=true")
            .build();
    }

    @ResponseBody
    @PostMapping("/auth/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
        var refreshToken = cookieHelper.getCookie(request, CookieHelper.REFRESH_COOKIE).orElse("");
        if (refreshToken.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var result = refreshTokenService.rotate(refreshToken, request.getHeader("User-Agent"), request.getRemoteAddr());
        if (!result.success) {
            cookieHelper.clearAuthCookies(response);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var userDetails = userDetailsService.loadUserByUsername(result.userEmail);
        var roles = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

        var newAccessToken = jwtService.generateAccessToken(userDetails.getUsername(), roles);

        cookieHelper.setAccessCookie(response, newAccessToken, Duration.ofMinutes(accessTtlMinutes));
        var ttl = Duration.between(java.time.LocalDateTime.now(), result.newExpiresAt);
        cookieHelper.setRefreshCookie(response, result.newRawToken, ttl);

        return ResponseEntity.noContent().build();
    }

    private boolean isFormLike(HttpServletRequest request) {
        var contentType = request.getContentType();
        var accept = request.getHeader("Accept");
        var xrw = request.getHeader("X-Requested-With");
        var formContent = contentType != null && contentType.startsWith(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        var htmlAccept = accept != null && accept.contains("text/html");
        var isAjax = "XMLHttpRequest".equalsIgnoreCase(xrw);
        return formContent || (htmlAccept && !isAjax);
    }

    @ResponseBody
    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        // Get the refresh token from cookie
        var refreshToken = cookieHelper
            .getCookie(request, CookieHelper.REFRESH_COOKIE)
            .orElse("");

        if (!refreshToken.isBlank()) {
            // Revoke the refresh token
            refreshTokenService.revokeCurrent(refreshToken);
        }

        // Clear authentication cookies
        cookieHelper.clearAuthCookies(response);

        return ResponseEntity.noContent().build();
    }
}

