package roomescape.reservationTime;

import java.time.LocalTime;

public class ReservationTime {
    private Long id;
    private LocalTime value;

    public ReservationTime(Long id, LocalTime value) {
        this.id = id;
        this.value = value;
    }

    public ReservationTime(LocalTime value) {
        this.value = value;
    }

    public ReservationTime() {

    }

    public Long getId() {
        return id;
    }

    public LocalTime getValue() {
        return value;
    }
}
