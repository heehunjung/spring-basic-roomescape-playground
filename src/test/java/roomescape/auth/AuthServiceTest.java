package roomescape.auth;

import static org.assertj.core.api.Assertions.*;
import static roomescape.auth.AuthService.SECRET_KEY;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.member.Member;
import roomescape.member.MemberDao;

@JdbcTest
class AuthServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MemberDao memberDao;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        memberDao = new MemberDao(jdbcTemplate);
        authService = new AuthService(memberDao);
    }

    @Test
    void generateAccessToken() {
        jdbcTemplate.update("INSERT INTO member (name, email, password, role) VALUES (?, ?, ?, ?)", "망고",
                "test@naver.com", "test1234", "admin");
        String accessToken = authService.generateAccessToken(new LoginRequest("test@naver.com", "test1234"));

        assertThat(accessToken).isNotBlank();
        assertThat(accessToken).isNotNull();
    }

    @Test
    void checkAccessToken() {
        jdbcTemplate.update("INSERT INTO member (name, email, password, role) VALUES (?, ?, ?, ?)", "망고",
                "test@naver.com", "test1234", "admin");
        Member member = new Member(0L, "망고", "test@naver.com", "admin");
        String jwt = Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();

        LoginCheckResponse response = authService.checkAccessToken(jwt);

        assertThat(response.name()).isEqualTo("망고");
    }
}
