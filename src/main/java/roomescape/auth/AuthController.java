package roomescape.auth;

import static roomescape.auth.session.jwt.JwtProvider.DEFAULT_TIME;
import static roomescape.auth.session.jwt.JwtProvider.EXPIRED_TOKEN;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.time.Duration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.auth.session.cookie.CookieResolver;
import roomescape.auth.session.cookie.CookieProvider;

@Controller
public class AuthController {

    private final AuthService authService;
    private final CookieProvider cookieProvider;
    private final CookieResolver cookieResolver;

    public AuthController(AuthService authService, CookieProvider cookieProvider, CookieResolver cookieResolver) {
        this.authService = authService;
        this.cookieProvider = cookieProvider;
        this.cookieResolver = cookieResolver;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequest loginRequest) {
        String accessToken = authService.generateAccessToken(loginRequest);
        ResponseCookie responseCookie = cookieProvider.generateCookie(accessToken, Duration.ofMinutes(DEFAULT_TIME));

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginCheckResponse> loginCheck(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String accessToken = cookieResolver.getToken(cookies);

        LoginCheckResponse result = authService.checkAccessToken(accessToken);
        return ResponseEntity.ok()
                .body(result);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie responseCookie = cookieProvider.generateCookie(EXPIRED_TOKEN, Duration.ZERO);

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .build();
    }
}
