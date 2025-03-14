package roomescape.auth.session;

import static roomescape.auth.session.JwtProvider.SECRET_KEY;
import static roomescape.auth.session.JwtProvider.NAME;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import roomescape.global.exception.RoomescapeUnauthorizedException;

@Component
public class JwtResolver {
    public String name(String token) {
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
        } catch (JwtException exception) {
            throw new RoomescapeUnauthorizedException("권한이 없습니다.");
        }
    }
}
