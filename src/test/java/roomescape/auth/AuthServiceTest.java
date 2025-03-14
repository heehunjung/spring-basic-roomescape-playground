package roomescape.auth;

import static java.lang.Thread.sleep;
import static org.assertj.core.api.Assertions.*;
import static roomescape.auth.session.jwt.JwtProvider.SECRET_KEY;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.member.Member;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AuthServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private AuthService authService;

    @Test
    void generateAccessToken() {
            jdbcTemplate.update("INSERT INTO member (name, email, password, role) VALUES (?, ?, ?, ?)", "비토",
                "test@naver.com", "test1234", "admin");
        String accessToken = authService.generateAccessToken(new LoginRequest("test@naver.com", "test1234"));

        assertThat(accessToken).isNotBlank();
        assertThat(accessToken).isNotNull();
    }

    @Test
    void checkAccessToken(){
        // 여기서 저장 안됨
        List<Map<String, Object>> allMembers = jdbcTemplate.queryForList("SELECT * FROM member");
        System.out.println("회원 전체 데이터: " + allMembers);
        jdbcTemplate.update("INSERT INTO member (name, email, password, role) VALUES (?, ?, ?, ?)", "망고",
                "test2@naver.com", "test1234", "admin");
        Long insertedId = jdbcTemplate.queryForObject("SELECT id FROM member WHERE email = 'test2@naver.com'", Long.class);
        // DB 상태 확인을 위해 member 테이블 전체 데이터를 조회하고 출력
        allMembers = jdbcTemplate.queryForList("SELECT * FROM member");
        System.out.println("회원 전체 데이터: " + allMembers);
        Member member = new Member(insertedId, "망고", "test2@naver.com", "admin");
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
