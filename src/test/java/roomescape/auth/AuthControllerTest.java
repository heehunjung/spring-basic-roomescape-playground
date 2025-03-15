package roomescape.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static roomescape.auth.LoginRequest.MAX_PASSWORD_LENGTH;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import roomescape.auth.session.cookie.CookieProvider;
import roomescape.auth.session.cookie.CookieResolver;
import roomescape.auth.session.jwt.JwtResolver;
import roomescape.member.MemberService;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @MockBean
    private MemberService memberService;
    @MockBean
    private JwtResolver jwtResolver;
    @MockBean
    private CookieResolver cookieResolver;
    @MockBean
    private AuthService authService;
    @MockBean
    private CookieProvider cookieProvider;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void validTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        LoginRequest loginRequest = new LoginRequest("example.com", "a".repeat(MAX_PASSWORD_LENGTH + 1));
        String json = mapper.writeValueAsString(loginRequest);

        MvcResult result = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andReturn();
        Exception resolvedException = result.getResolvedException();
        MethodArgumentNotValidException exception = (MethodArgumentNotValidException) resolvedException;

        List<String> list = getExceptionMessages(exception);
        assertThat(list).containsExactlyInAnyOrder(
                "[email] 이메일 양식에 맞지 않습니다.",
                "[password] 비밀번호는 20자 이하여야 합니다.");
    }

    private List<String> getExceptionMessages(MethodArgumentNotValidException exception) {

        return exception.getBindingResult().getFieldErrors().stream()
                .map(error -> String.format("[%s] %s", error.getField(), error.getDefaultMessage()))
                .toList();
    }
}
