package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.exception.RoomescapeUnauthorizedException;


@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        String accessToken = authService.generateAccessToken(loginRequest);

        Cookie cookie = new Cookie("token", accessToken);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity loginCheck(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String accessToken = getToken(cookies);
        if (accessToken == null) {
            throw new RoomescapeUnauthorizedException("Invalid accessToken");
        }

        LoginCheckResponse result = authService.checkAccessToken(accessToken);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    private static String getToken(Cookie[] cookies) {
        String accessToken = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                accessToken = cookie.getValue();
            }
        }
        return accessToken;
    }
}
