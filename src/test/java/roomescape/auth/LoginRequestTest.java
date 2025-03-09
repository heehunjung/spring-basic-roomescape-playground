package roomescape.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

@WebMvcTest(AuthController.class)
class LoginRequestTest {

    @MockBean
    private AuthService authService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void validTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        LoginRequest loginRequest = new LoginRequest("example.com", "a".repeat(256));
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
                "[password] 이메일 양식에 맞지 않습니다.");
    }

    private List<String> getExceptionMessages(MethodArgumentNotValidException exception) {

        return exception.getBindingResult().getFieldErrors().stream()
                .map(error -> String.format("[%s] %s", error.getField(), error.getDefaultMessage()))
                .toList();
    }
}
