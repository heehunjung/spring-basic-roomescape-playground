package roomescape.auth.session.cookie;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.auth.session.cookie.CookieProvider.PATH;
import static roomescape.auth.session.jwt.JwtProvider.EXPIRED_TOKEN;

import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseCookie;

class CookieProviderTest {

    private CookieProvider cookieProvider;

    @BeforeEach
    void setUp() {
        cookieProvider = new CookieProvider();
    }

    @Nested
    class GenerateCookieTests {
        @Test
        void 기본_쿠키를_생성한다() {
            String value = "value";
            ResponseCookie cookie = cookieProvider.generateCookie(value, Duration.ofHours(1));

            assertThat(cookie.toString())
                    .contains("token=" + value)
                    .contains("Max-Age=" + Duration.ofHours(1).getSeconds())
                    .contains("Path=" + PATH);
        }

        @Test
        void 만료된_쿠키를_생성한다() {
            String value = EXPIRED_TOKEN;
            ResponseCookie cookie = cookieProvider.generateCookie(value, Duration.ZERO);

            assertThat(cookie.toString())
                    .contains("token=" + value)
                    .contains("Max-Age=0")
                    .contains("Path=" + PATH);
        }
    }
}
