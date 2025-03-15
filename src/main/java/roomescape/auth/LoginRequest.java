package roomescape.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotNull(message = "이메일은 필수 입력값입니다.")
        @NotBlank(message = "이메일은 비어 있을 수 없습니다.")
        @Email(message = "이메일 양식에 맞지 않습니다.")
        @Size(max = MAX_EMAIL_LENGTH, message = "이메일은 255자 이하여야 합니다.")
        String email,

        @NotNull(message = "비밀번호는 필수 입력값입니다.")
        @NotBlank(message = "비밀번호는 비어 있을 수 없습니다.")
        @Size(max = MAX_PASSWORD_LENGTH, message = "비밀번호는 20자 이하여야 합니다.")
        String password
) {
        public static final int MAX_PASSWORD_LENGTH = 20;
        public static final int MAX_EMAIL_LENGTH = 255;
}
