package roomescape.exception;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleInvalidFormatException(MethodArgumentNotValidException exception) {
        List<String> errors = new ArrayList<>();
        exception.getBindingResult().getFieldErrors().forEach(fieldError -> {
            errors.add("[" + fieldError.getField() + "] " + fieldError.getDefaultMessage());
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }
}
