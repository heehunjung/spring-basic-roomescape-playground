package roomescape.auth;

import static roomescape.auth.session.jwt.JwtProvider.SECRET_KEY;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import roomescape.auth.session.jwt.JwtProvider;
import roomescape.global.exception.RoomescapeUnauthorizedException;
import roomescape.member.Member;
import roomescape.member.MemberDao;

@Service
public class AuthService {

    private final MemberDao memberDao;
    private final JwtProvider jwtProvider;

    public AuthService(MemberDao memberDao, JwtProvider jwtProvider) {
        this.memberDao = memberDao;
        this.jwtProvider = jwtProvider;
    }

    public String generateAccessToken(LoginRequest loginRequest) {
        String email = loginRequest.email();
        String password = loginRequest.password();
        Member findMember = memberDao.findByEmailAndPassword(email, password);

        if (findMember == null) {
            throw new RoomescapeUnauthorizedException("회원 정보를 찾을 수 없습니다.");
        }

        return jwtProvider.generateToken(findMember.getId(), findMember.getName(), findMember.getRole());
    }

    public LoginCheckResponse checkAccessToken(String accessToken) {
        String name = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(accessToken)
                .getBody()
                .get("name", String.class);
        try {
            memberDao.findByName(name);
            return new LoginCheckResponse(name);
        } catch (IncorrectResultSizeDataAccessException exception) {
            throw new RoomescapeUnauthorizedException("회원 정보를 찾을 수 없습니다." + name);
        }
    }
}
