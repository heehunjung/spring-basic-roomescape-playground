package roomescape.auth.session.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.auth.session.jwt.JwtProvider.SECRET_KEY;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.global.exception.RoomescapeUnauthorizedException;

class JwtResolverTest {

    private JwtResolver jwtResolver;

    @BeforeEach
    void setUp() {
        jwtResolver = new JwtResolver();
    }

    @Nested
    class resolveTokenTest {
        @Test
        void 정상적인_토큰을_해석한다() {
            String token = Jwts.builder()
                    .setSubject("1")
                    .claim("name", "어드민")
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1시간 유효
                    .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                    .compact();
            String name = jwtResolver.resolveToken(token);

            assertThat(name).isEqualTo("어드민");
        }

        @Test
        void 만료된_토큰이면_예외가_발생한다() {
            String expiredToken = Jwts.builder()
                    .setSubject("1")
                    .claim("name", "어드민")
                    .setExpiration(new Date(System.currentTimeMillis() - 1000 * 60)) // 1분 전에 만료
                    .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                    .compact();

            assertThatThrownBy(() -> jwtResolver.resolveToken(expiredToken))
                    .isInstanceOf(RoomescapeUnauthorizedException.class)
                    .hasMessage("만료된 토큰입니다.");
        }

        @Test
        void 잘못된_토큰이면_예외가_발생한다() {
            String invalidToken = "invalidToken";

            assertThatThrownBy(() -> jwtResolver.resolveToken(invalidToken))
                    .isInstanceOf(RoomescapeUnauthorizedException.class)
                    .hasMessage("잘못된 토큰입니다. 다시 로그인 해주세요.");
        }
    }
}