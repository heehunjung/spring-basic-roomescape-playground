package roomescape.reservation;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.theme.Theme;
import roomescape.reservationTime.ReservationTime;

public record ReservationRequest(String name,
                                 @NotNull
                                 LocalDate date,
                                 long themeId,
                                 long timeId) {

    public Reservation toReservation(Theme theme, ReservationTime reservationTime) {
        return new Reservation(name, date, reservationTime, theme);
    }

    public ReservationRequest update(String name) {
        return new ReservationRequest(name, this.date, this.themeId, this.timeId);
    }
}
