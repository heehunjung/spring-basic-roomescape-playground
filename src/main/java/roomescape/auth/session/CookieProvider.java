package roomescape.auth.session;

import static roomescape.auth.session.JwtProvider.TOKEN;

import java.time.Duration;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieProvider {

    public static final String PATH = "/";

    public ResponseCookie generateCookie(String value, Duration duration) {
        ResponseCookie responseCookie = ResponseCookie.from(TOKEN, value)
                .path(PATH)
                .httpOnly(true)
                .maxAge(duration)
                .build();
        return responseCookie;
    }
}