package roomescape.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberDao;

@Service
public class AuthService {

    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    private final MemberDao memberDao;

    public AuthService(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public String generateAccessToken(LoginRequest loginRequest) {
        String email = loginRequest.email();
        String password = loginRequest.password();
        Member findMember = memberDao.findByEmailAndPassword(email, password);

        if (findMember == null) {
            throw new RuntimeException("로그인 실패입니다.");
        }

        return Jwts.builder()
                .setSubject(findMember.getId().toString())
                .claim("name", findMember.getName())
                .claim("role", findMember.getRole())
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

    public LoginCheckResponse checkAccessToken(String accessToken) {
        String name = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(accessToken)
                .getBody()
                .get("name", String.class);

        if (memberDao.findByName(name) == null) {
            throw new RuntimeException("can not find member");
        }

        return new LoginCheckResponse(name);
    }
}
