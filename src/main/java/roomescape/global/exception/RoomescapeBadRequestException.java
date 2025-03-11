package roomescape.global.exception;

import org.springframework.http.HttpStatus;

public class RoomescapeBadRequestException extends RoomescapeException {

    public RoomescapeBadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
