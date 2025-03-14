package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;
import roomescape.auth.AuthService;
import roomescape.auth.LoginRequest;
import roomescape.auth.session.jwt.JwtResolver;
import roomescape.member.MemberDao;
import roomescape.reservation.ReservationDao;
import roomescape.reservation.ReservationResponse;
import roomescape.reservationTime.ReservationTimeDao;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MissionStepTest {

    @Autowired
    private AuthService authService;

    @LocalServerPort
    private int randomPort;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private ReservationDao reservationDao;
    @Autowired
    private ReservationTimeDao reservationTimeDao;
    @Autowired
    private JwtResolver jwtResolver;

    @BeforeEach
    void setUp() {
        RestAssured.port = randomPort;
    }

    @Test
    void 일단계() {
        Map<String, String> params = new HashMap<>();
        params.put("email", "admin@email.com");
        params.put("password", "password");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(204)
                .extract();

        String token = response.headers().get("Set-Cookie").getValue().split(";")[0].split("=")[1];
        assertThat(token).isNotBlank();

        ExtractableResponse<Response> checkResponse = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(200)
                .extract();

        assertThat(checkResponse.body().jsonPath().getString("name")).isEqualTo("어드민");
    }

    @Test
    void 이단계() {
        String token = authService.generateAccessToken(new LoginRequest("admin@email.com", "password"));

        Map<String, String> params = new HashMap<>();
        params.put("date", "2024-03-01");
        params.put("time", "1");
        params.put("theme", "1");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .post("/reservations")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(201);
        assertThat(response.as(ReservationResponse.class).name()).isEqualTo("어드민");

        params.put("name", "브라운");

        ExtractableResponse<Response> adminResponse = RestAssured.given().log().all()
                .body(params)
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .post("/reservations")
                .then().log().all()
                .extract();

        assertThat(adminResponse.statusCode()).isEqualTo(201);
        assertThat(adminResponse.as(ReservationResponse.class).name()).isEqualTo("브라운");
    }
}

// 문제 상황
//MissionStepTest 에서도 @SpringBootTest
/**
 * MissionStepTest 에서 @SpringBootTest 사용해서 ApplicationContext 생성
 * AuthServiceTest 에서도 @SpringBootTest 사용해서 ApplicationContext 생성함
 * 근데 webEnvironment 설정이 달라서 -> 캐시된 ApplicationContext 을  사용하지 않고 다른 테스트로 넘어갈 때 ApplicationContext 를 새로 만듦
 * ApplicationContext 를  새로 만들 때 db도 새로 연결함. 이 과정에서 schema.sql 이 한번 더 실행되면서 insert 문에서 unique 제약 조건에 따른 오류가 발생 중
 * 테스트 실행 순서마다 결과가 바뀜  SERVICE 가 늦게 실행되면 초기화가 잘 안됨 메서드 사이에서
 * */
