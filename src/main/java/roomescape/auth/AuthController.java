package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.global.exception.RoomescapeUnauthorizedException;


@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response) {
        String accessToken = authService.generateAccessToken(loginRequest);

        Cookie cookie = new Cookie("token", accessToken);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginCheckResponse> loginCheck(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String accessToken = getToken(cookies);
        if (accessToken == null) {
            throw new RoomescapeUnauthorizedException("로그인 되어있지 않습니다.");
        }

        LoginCheckResponse result = authService.checkAccessToken(accessToken);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    private String getToken(Cookie[] cookies) {
        String accessToken = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                accessToken = cookie.getValue();
            }
        }
        return accessToken;
    }
}
