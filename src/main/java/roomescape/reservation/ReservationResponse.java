package roomescape.reservation;

import java.time.format.DateTimeFormatter;

public record ReservationResponse(Long id,
                                  String name,
                                  String theme,
                                  String date,
                                  String time) {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public ReservationResponse(Reservation reservation) {
        this(reservation.getId(), reservation.getName(), reservation.getTheme().getName(),
                reservation.getDateValue(), reservation.getTimeValue().formatted(TIME_FORMATTER));
    }
}
