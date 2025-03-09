package roomescape.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(Exception.class)
    public ResponseEntity handleRuntimeException(Exception exception) {
        exception.printStackTrace();
        return ResponseEntity.internalServerError()
                .body("서버 에러 관리자에게 문의해주세요.");
    }

    @ExceptionHandler(RoomescapeException.class)
    public ResponseEntity handleRoomescapeException(RoomescapeException exception) {
        exception.printStackTrace();

        return ResponseEntity.status(exception.getStatus())
                .body(exception.getMessage());
    }
}
