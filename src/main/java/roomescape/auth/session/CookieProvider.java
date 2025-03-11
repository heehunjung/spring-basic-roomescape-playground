package roomescape.auth.session;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieProvider {

    public static final String PATH = "/";

    public ResponseCookie generateCookie(String key, String value) {
        ResponseCookie responseCookie = ResponseCookie.from(key, value)
                .path(PATH)
                .httpOnly(true)
                .maxAge(duration)
                .build();
        return responseCookie;
    }
}