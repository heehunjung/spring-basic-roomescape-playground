package roomescape.global.exception;

import org.springframework.http.HttpStatus;

public class RoomescapeUnauthorizedException extends RoomescapeException {

    public RoomescapeUnauthorizedException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
