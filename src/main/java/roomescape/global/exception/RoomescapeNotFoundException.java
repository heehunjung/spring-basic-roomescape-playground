package roomescape.global.exception;

import org.springframework.http.HttpStatus;

public class RoomescapeNotFoundException extends RoomescapeException {

    public RoomescapeNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
