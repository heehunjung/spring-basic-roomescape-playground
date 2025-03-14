package roomescape.reservation;

import java.time.LocalDate;
import roomescape.theme.Theme;
import roomescape.reservationTime.ReservationTime;

public class Reservation {
    private Long id;
    private String name;
    private LocalDate date;
    private ReservationTime reservationTime;
    private Theme theme;

    public Reservation(Long id, String name, LocalDate date, ReservationTime reservationTime, Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.reservationTime = reservationTime;
        this.theme = theme;
    }

    public Reservation(String name, LocalDate date, ReservationTime reservationTime, Theme theme) {
        this.name = name;
        this.date = date;
        this.reservationTime = reservationTime;
        this.theme = theme;
    }

    public Reservation() {

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDateValue() {
        return date.toString();
    }

    public ReservationTime getTime() {
        return reservationTime;
    }

    public String getTimeValue() {
        return reservationTime.getValue()
                .toString();
    }

    public Theme getTheme() {
        return theme;
    }

    public String getThemeValue() {
        return theme.getName();
    }
}
