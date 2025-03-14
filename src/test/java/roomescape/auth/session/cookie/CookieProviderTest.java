package roomescape.auth.session;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.auth.session.CookieProvider.PATH;

import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseCookie;

class CookieProviderTest {

    private CookieProvider cookieProvider;

    @BeforeEach
    void setUp() {
        cookieProvider = new CookieProvider();
    }

    @Test
    void generateCookieTest() {
        CookieProvider cookieProvider = new CookieProvider();
        String value = "value";
        ResponseCookie cookie = cookieProvider.generateCookie(value, Duration.ofHours(1));

        assertThat(cookie.toString())
                .contains("token=" + value)
                .contains("Max-Age=" + Duration.ofHours(1).getSeconds())
                .contains("Path=" + PATH);
    }
}