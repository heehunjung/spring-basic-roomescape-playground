package roomescape.auth.session;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import roomescape.member.Member;

@Component
public class JwtProvider {

    public static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
    public static final String NAME = "name";
    public static final String ROLE = "role";

    public String generateToken(Long id, String name, String role) {
        return Jwts.builder()
                .setSubject(id.toString())
                .claim(NAME, name)
                .claim(ROLE, role)
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }
}
