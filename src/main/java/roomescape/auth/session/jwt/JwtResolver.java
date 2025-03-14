package roomescape.auth.session.jwt;

import static roomescape.auth.session.jwt.JwtProvider.SECRET_KEY;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import roomescape.global.exception.RoomescapeUnauthorizedException;

@Component
public class JwtResolver {

    public String resolveToken(String token) {
        try {
            String name = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("name", String.class);
            return name;
        } catch (ExpiredJwtException exception) {
            throw new RoomescapeUnauthorizedException("만료된 토큰입니다.");
        } catch (Exception exception) {
            throw new RoomescapeUnauthorizedException("잘못된 토큰입니다. 다시 로그인 해주세요.");
        }
    }
}
