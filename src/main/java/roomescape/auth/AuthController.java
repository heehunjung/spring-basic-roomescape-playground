package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.time.Duration;
import java.util.Arrays;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.auth.session.CookieProvider;
import roomescape.auth.session.JwtProvider;
import roomescape.global.exception.RoomescapeUnauthorizedException;


@Controller
public class AuthController {

    public static final String TOKEN = "token";
    public static final String EXPIRED_TOKEN = "";

    private final AuthService authService;
    private final CookieProvider cookieProvider;

    public AuthController(AuthService authService, CookieProvider cookieProvider) {
        this.authService = authService;
        this.cookieProvider = cookieProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequest loginRequest) {
        String accessToken = authService.generateAccessToken(loginRequest);
        ResponseCookie responseCookie = cookieProvider.generateCookie(TOKEN, accessToken, Duration.ofMinutes(60));

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginCheckResponse> loginCheck(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String accessToken = getToken(cookies);

        LoginCheckResponse result = authService.checkAccessToken(accessToken);
        return ResponseEntity.ok()
                .body(result);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie responseCookie = cookieProvider.generateCookie(TOKEN, EXPIRED_TOKEN, Duration.ZERO);

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .build();
    }

    private String getToken(Cookie[] cookies) {
        String accessToken = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(TOKEN))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);

        if (accessToken == null) {
            throw new RoomescapeUnauthorizedException("로그인 되어있지 않습니다.");
        }
        return accessToken;
    }
}
